import { Link, useParams } from 'react-router-dom';
import { fetchPost } from '../api/posts';
import { useAsync } from '../hooks/useAsync';
import Layout from '../components/Layout';

const errorClass = 'rounded border-l-4 border-error bg-error-bg text-error px-3 py-2.5';
const h1Class = 'text-[32px] font-medium tracking-[-0.5px] text-text-h mt-6 mb-4';
const linkClass = 'text-accent hover:text-accent-hover no-underline';
const tagPillClass = 'inline-block rounded-full bg-accent/10 text-accent text-xs font-medium px-2.5 py-1';

function PostDetailPage() {
  const { id } = useParams<{ id: string }>();
  const { data: post, isLoading, error } = useAsync(() => fetchPost(Number(id)), [id]);

  if (isLoading) {
    return (
      <Layout>
        <p>読み込み中...</p>
      </Layout>
    );
  }

  if (error) {
    return (
      <Layout>
        <p className={errorClass}>エラーが発生しました: {error}</p>
      </Layout>
    );
  }

  if (!post) {
    return (
      <Layout>
        <p>投稿が見つかりません</p>
      </Layout>
    );
  }

  return (
    <Layout>
      <Link to="/" className={linkClass}>
        投稿一覧に戻る
      </Link>
      <h1 className={h1Class}>{post.title}</h1>
      {post.tags.length > 0 && (
        <div className="mb-4 flex flex-wrap gap-1.5">
          {post.tags.map((tag) => (
            <span key={tag.id} className={tagPillClass}>
              {tag.name}
            </span>
          ))}
        </div>
      )}
      <p className="text-text-h">{post.body}</p>
    </Layout>
  );
}

export default PostDetailPage;
