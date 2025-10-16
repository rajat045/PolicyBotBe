CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS documents (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  filename text,
  uploaded_by text,
  uploaded_at timestamptz default now(),
  metadata jsonb
);

CREATE TABLE IF NOT EXISTS document_chunks (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  doc_id uuid REFERENCES documents(id) ON DELETE CASCADE,
  chunk_index int,
  chunk_text text,
  embedding vector(1536),
  metadata jsonb,
  created_at timestamptz default now()
);

CREATE INDEX IF NOT EXISTS idx_chunks_embedding ON document_chunks USING ivfflat (embedding vector_cosine_ops) WITH (lists = 100);
