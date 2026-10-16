package com.resolve.resolve_backend.issue;

import com.resolve.resolve_backend.issue.dto.CreateIssueRequest;
import com.resolve.resolve_backend.issue.dto.IssueResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueResponse createIssue(
            @Valid @RequestBody CreateIssueRequest request) {
        return issueService.createIssue(request);
    }

    @GetMapping
    public List<IssueResponse> getAllIssues() {
        return issueService.getAllIssues();
    }

    @GetMapping("/{id}")
    public IssueResponse getIssueById(@PathVariable Long id) {
        return issueService.getIssueById(id);
    }

    @GetMapping
    public Page<IssueResponse> getAllIssues(
            @RequestParam(required = false) IssueStatus status,
            @RequestParam(required = false) IssuePriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return issueService.searchIssues(
                status, priority, page, size, sortBy, direction
        );
    }

}