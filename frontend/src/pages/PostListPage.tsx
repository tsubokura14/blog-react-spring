import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchPosts, type Post } from '../api/posts';

function PostListPage() {
  const [posts, setPosts] = useState<Post[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchPosts()
      .then(setPosts)
      .catch((err: unknown) => {
        setError(err instanceof Error ? err.message : String(err));
      })
      .finally(() => setIsLoading(false));
  }, []);

  if (isLoading) {
    return <p>読み込み中...</p>;
  }

  if (error) {
    return <p>エラーが発生しました: {error}</p>;
  }

  return (
    <div>
      <h1>投稿一覧</h1>
      <Link to="/posts/new">新規投稿</Link>
      {posts.length === 0 ? (
        <p>投稿がありません</p>
      ) : (
        <ul>
          {posts.map((post) => (
            <li key={post.id}>
              <Link to={`/posts/${post.id}`}>{post.title}</Link>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default PostListPage;
