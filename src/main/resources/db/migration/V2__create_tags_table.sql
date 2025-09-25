-- Create tags table
CREATE TABLE tags (
      id UUID PRIMARY KEY,
      created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
      workspace_id UUID NOT NULL REFERENCES workspaces(id),
      created_by UUID NOT NULL,
      name VARCHAR(255) NOT NULL,
      description VARCHAR(255),
      status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'ARCHIVED'))
);