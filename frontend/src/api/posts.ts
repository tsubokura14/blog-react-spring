// 空文字にして相対パス(/posts)でリクエストする。
// 実際の送信先はvite.config.tsのserver.proxyがhttp://localhost:8080へ転送する
// (ブラウザからは同一オリジンへのリクエストに見えるため、CORS設定が不要になる)
const API_BASE_URL = '';

// backendのPostResponse(id/title/body)と対応する型。
// PUT/DELETEはこのブランチのbackendにまだ無いため、この層でも扱っていない
export type Post = {
  id: number;
  title: string;
  body: string;
};

// backendのPostCreateRequest(title/body)と対応する型
export type PostCreateInput = {
  title: string;
  body: string;
};

// GET /posts (一覧取得)
export async function fetchPosts(): Promise<Post[]> {
  const res = await fetch(`${API_BASE_URL}/posts`);
  if (!res.ok) {
    throw new Error(`投稿一覧の取得に失敗しました (status: ${res.status})`);
  }
  return res.json();
}

// GET /posts/{id} (単体取得)
export async function fetchPost(id: number): Promise<Post> {
  const res = await fetch(`${API_BASE_URL}/posts/${id}`);
  if (!res.ok) {
    throw new Error(`投稿の取得に失敗しました (status: ${res.status})`);
  }
  return res.json();
}

// POST /posts (作成)
export async function createPost(input: PostCreateInput): Promise<Post> {
  const res = await fetch(`${API_BASE_URL}/posts`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  if (!res.ok) {
    throw new Error(`投稿の作成に失敗しました (status: ${res.status})`);
  }
  return res.json();
}
