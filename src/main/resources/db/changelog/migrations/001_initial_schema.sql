-- liquibase formatted sql

-- changeset crm:001-create-users-table
CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  avatar TEXT,
  role VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN', 'MANAGER', 'MEMBER')),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- changeset crm:002-create-pipelines-table
CREATE TABLE IF NOT EXISTS pipelines (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name VARCHAR(255) NOT NULL,
  description TEXT,
  steps JSONB NOT NULL DEFAULT '[]',
  deleted_steps JSONB NOT NULL DEFAULT '[]',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_pipelines_name ON pipelines(name);

-- changeset crm:003-create-tasks-table
CREATE TABLE IF NOT EXISTS tasks (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  pipeline_id UUID NOT NULL REFERENCES pipelines(id) ON DELETE CASCADE,
  step_id VARCHAR(255) NOT NULL,
  form_data JSONB NOT NULL DEFAULT '{}',
  form_fields_snapshot JSONB NOT NULL DEFAULT '[]',
  status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED')),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_tasks_pipeline ON tasks(pipeline_id);
CREATE INDEX IF NOT EXISTS idx_tasks_status ON tasks(status);
CREATE INDEX IF NOT EXISTS idx_tasks_created ON tasks(created_at DESC);

-- changeset crm:004-create-notes-table
CREATE TABLE IF NOT EXISTS notes (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  created_by_id UUID REFERENCES users(id) ON DELETE SET NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  tags JSONB,
  related_entity_id VARCHAR(255),
  related_entity_type VARCHAR(50) CHECK (related_entity_type IN ('TASK', 'PIPELINE', 'GLOBAL'))
);

CREATE INDEX IF NOT EXISTS idx_notes_created_by ON notes(created_by_id);
CREATE INDEX IF NOT EXISTS idx_notes_related ON notes(related_entity_id, related_entity_type);

-- changeset crm:005-create-global-tasks-table
CREATE TABLE IF NOT EXISTS global_tasks (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  title VARCHAR(255) NOT NULL,
  description TEXT,
  assigned_to_id UUID REFERENCES users(id) ON DELETE SET NULL,
  created_by_id UUID REFERENCES users(id) ON DELETE SET NULL,
  status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED')),
  priority VARCHAR(50) NOT NULL CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
  due_date TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  related_entity_id VARCHAR(255),
  related_entity_type VARCHAR(50) CHECK (related_entity_type IN ('PIPELINE', 'GLOBAL'))
);

CREATE INDEX IF NOT EXISTS idx_global_tasks_assigned ON global_tasks(assigned_to_id);
CREATE INDEX IF NOT EXISTS idx_global_tasks_created_by ON global_tasks(created_by_id);
CREATE INDEX IF NOT EXISTS idx_global_tasks_status ON global_tasks(status);
CREATE INDEX IF NOT EXISTS idx_global_tasks_priority ON global_tasks(priority);
CREATE INDEX IF NOT EXISTS idx_global_tasks_due ON global_tasks(due_date);
