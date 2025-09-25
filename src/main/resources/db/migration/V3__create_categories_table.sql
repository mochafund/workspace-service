CREATE TABLE categories (
      id UUID PRIMARY KEY,
      created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
      workspace_id UUID NOT NULL REFERENCES workspaces(id),
      created_by UUID NOT NULL,
      parent_id UUID REFERENCES categories(id) ON DELETE SET NULL,
      name VARCHAR(255) NOT NULL,
      description VARCHAR(255),
      status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'ARCHIVED')),
      is_income BOOLEAN NOT NULL DEFAULT FALSE,
      exclude_from_budget BOOLEAN NOT NULL DEFAULT FALSE,
      exclude_from_totals BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_categories_workspace_id ON categories(workspace_id);
CREATE INDEX idx_categories_parent_id ON categories(parent_id);
CREATE INDEX idx_categories_workspace_parent ON categories(workspace_id, parent_id);