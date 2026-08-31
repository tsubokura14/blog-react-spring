import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { fetchPost, type Post } from '../api/posts';

function PostDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [post, setPost] = useState<Post | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) {
      return;
    }
    fetchPost(Number(id))
      .then(setPost)
      .catch((err: unknown) => {
        setError(err instanceof Error ? err.message : String(err));
      })
      .finally(() => setIsLoading(false));
  }, [id]);

  if (isLoading) {
    return <p>読み込み中...</p>;
  }

  if (error) {
    return <p>エラーが発生しました: {error}</p>;
  }

  if (!post) {
    return <p>投稿が見つかりません</p>;
  }

  return (
    <div>
      <Link to="/">投稿一覧に戻る</Link>
      <h1>{post.title}</h1>
      <p>{post.body}</p>
    </div>
  );
}

export default PostDetailPage;
