package com.resolve.resolve_backend.issue;

import com.resolve.resolve_backend.issue.dto.CreateIssueRequest;
import com.resolve.resolve_backend.issue.dto.IssueResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}