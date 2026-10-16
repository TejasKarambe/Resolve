package com.resolve.resolve_backend.issue.dto;

import com.resolve.resolve_backend.issue.IssuePriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateIssueRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 150, message = "Title cannot exceed 150 characters")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Customer name is required")
        @Size(max = 100, message = "Customer name cannot exceed 100 characters")
        String customerName,

        @NotBlank(message = "Customer phone is required")
        @Size(max = 20, message = "Customer phone cannot exceed 20 characters")
        String customerPhone,

        @NotBlank(message = "Service address is required")
        @Size(max = 500, message = "Service address cannot exceed 500 characters")
        String serviceAddress,

        @NotNull(message = "Priority is required")
        IssuePriority priority

) {
}