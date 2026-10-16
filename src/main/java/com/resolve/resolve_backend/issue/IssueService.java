package com.resolve.resolve_backend.issue;

import com.resolve.resolve_backend.issue.dto.CreateIssueRequest;
import com.resolve.resolve_backend.issue.dto.IssueResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import java.util.Set;


import java.util.List;

@Service
public class IssueService {

    private final IssueRepository issueRepository;

    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Transactional
    public IssueResponse createIssue(CreateIssueRequest request) {
        Issue issue = new Issue();

        issue.setTitle(request.title());
        issue.setDescription(request.description());
        issue.setCustomerName(request.customerName());
        issue.setCustomerPhone(request.customerPhone());
        issue.setServiceAddress(request.serviceAddress());
        issue.setPriority(request.priority());

        // Status remains NEW, set by the entity.

        Issue savedIssue = issueRepository.save(issue);

        return toResponse(savedIssue);
    }

    @Transactional(readOnly = true)
    public List<IssueResponse> getAllIssues() {
        return issueRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public IssueResponse getIssueById(Long id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() ->
                        new IssueNotFoundException(
                                "Issue not found with id: " + id
                        )
                );

        return toResponse(issue);
    }

    private IssueResponse toResponse(Issue issue) {
        return new IssueResponse(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getCustomerName(),
                issue.getCustomerPhone(),
                issue.getServiceAddress(),
                issue.getStatus(),
                issue.getPriority(),
                issue.getCreatedAt(),
                issue.getUpdatedAt()
        );
    }


    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("id", "title", "status", "priority", "createdAt", "updatedAt");

    @Transactional(readOnly = true)
    public Page<IssueResponse> searchIssues(
            IssueStatus status,
            IssuePriority priority,
            int page,
            int size,
            String sortBy,
            String direction) {

        if (page < 0) {
            throw new IllegalArgumentException("Page cannot be negative.");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Size must be between 1 and 100.");
        }

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Unsupported sort field: " + sortBy);
        }

        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Direction must be 'asc' or 'desc'.");
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        Specification<Issue> specification = (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (status != null) {
                predicates = cb.and(
                        predicates,
                        cb.equal(root.get("status"), status)
                );
            }

            if (priority != null) {
                predicates = cb.and(
                        predicates,
                        cb.equal(root.get("priority"), priority)
                );
            }

            return predicates;
        };

        return issueRepository.findAll(specification, pageable)
                .map(this::toResponse);
    }

}