-- Create accounts table
CREATE TABLE accounts (
      id UUID PRIMARY KEY,
      created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
      workspace_id UUID NOT NULL REFERENCES workspaces(id) ON DELETE CASCADE,
      created_by UUID NOT NULL,
      balance DECIMAL(10, 2) NOT NULL DEFAULT 0,
      name VARCHAR(255) NOT NULL,
      display_name VARCHAR(255),
      currency VARCHAR(3) NOT NULL DEFAULT 'USD',
      institution_name VARCHAR(255),
      status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'ARCHIVED')),
      source VARCHAR(20) NOT NULL DEFAULT 'MANUAL' CHECK (status IN ('MANUAL', 'AUTOMATIC')),
      type VARCHAR(255) NOT NULL,
      sub_type VARCHAR(255) NOT NULL
);

-- TODO: Ensure balance is correct
-- TODO: Add Check for currency field
-- TODO: Add Check for type field