const API_BASE_URL = '';

// backendのTagResponse(id/name)と対応する型
export type Tag = {
  id: number;
  name: string;
};

// GET /tags (一覧取得)
export async function fetchTags(): Promise<Tag[]> {
  const res = await fetch(`${API_BASE_URL}/tags`);
  if (!res.ok) {
    throw new Error(`タグ一覧の取得に失敗しました (status: ${res.status})`);
  }
  return res.json();
}
