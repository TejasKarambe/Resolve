package com.resolve.resolve_backend.issue.dto;

import com.resolve.resolve_backend.issue.IssuePriority;
import com.resolve.resolve_backend.issue.IssueStatus;

import java.time.LocalDateTime;

public record IssueResponse(
        Long id,
        String title,
        String description,
        String customerName,
        String customerPhone,
        String serviceAddress,
        IssueStatus status,
        IssuePriority priority,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}