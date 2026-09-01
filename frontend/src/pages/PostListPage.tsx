import { Link } from 'react-router-dom';
import { fetchPosts } from '../api/posts';
import { useAsync } from '../hooks/useAsync';
import Layout from '../components/Layout';
import { buttonClass } from '../components/Button';

const errorClass = 'rounded border-l-4 border-error bg-error-bg text-error px-3 py-2.5';
const h1Class = 'text-[32px] font-medium tracking-[-0.5px] text-text-h mt-6 mb-4';
const tagPillClass = 'inline-block rounded-full bg-accent/10 text-accent text-xs font-medium px-2.5 py-1';

function PostListPage() {
  const { data: posts, isLoading, error } = useAsync(() => fetchPosts(), []);

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

  return (
    <Layout>
      <h1 className={h1Class}>投稿一覧</h1>
      <Link to="/posts/new" className={buttonClass}>
        新規投稿
      </Link>
      {posts && posts.length === 0 ? (
        <p>投稿がありません</p>
      ) : (
        <ul className="mt-4 flex flex-col gap-2.5">
          {posts?.map((post) => (
            <li
              key={post.id}
              className="rounded-lg border border-border border-l-4 border-l-accent bg-surface p-3.5 px-4 shadow-sm transition-[box-shadow,transform] hover:shadow-md hover:-translate-y-px"
            >
              <Link to={`/posts/${post.id}`} className="block font-medium text-text-h no-underline hover:text-accent">
                {post.title}
              </Link>
              {post.tags.length > 0 && (
                <div className="mt-1.5 flex flex-wrap gap-1.5">
                  {post.tags.map((tag) => (
                    <span key={tag.id} className={tagPillClass}>
                      {tag.name}
                    </span>
                  ))}
                </div>
              )}
            </li>
          ))}
        </ul>
      )}
    </Layout>
  );
}

export default PostListPage;
