import { useState } from 'react';
import type { FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { createPost } from '../api/posts';
import { fetchTags } from '../api/tags';
import { useAsync } from '../hooks/useAsync';
import Layout from '../components/Layout';
import Button from '../components/Button';

const errorClass = 'rounded border-l-4 border-error bg-error-bg text-error px-3 py-2.5';
const h1Class = 'text-[32px] font-medium tracking-[-0.5px] text-text-h mt-6 mb-4';
const fieldClass = 'mb-4 flex flex-col gap-1.5 text-left';
const labelClass = 'text-sm font-medium text-text-h';
const inputClass =
  'rounded-md border border-border bg-surface text-text-h px-3 py-2.5 outline-none ' +
  'transition-[border-color,box-shadow] focus-visible:border-accent focus-visible:ring-[3px] focus-visible:ring-accent/20';
const checkboxLabelClass = 'flex items-center gap-1.5 text-sm text-text-h';

// 編集(PUT)はbackendに未実装のため、このページは投稿作成のみ対応する
function PostFormPage() {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [body, setBody] = useState('');
  const [selectedTagIds, setSelectedTagIds] = useState<number[]>([]);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const { data: tags } = useAsync(() => fetchTags(), []);

  const toggleTag = (tagId: number) => {
    setSelectedTagIds((prev) =>
      prev.includes(tagId) ? prev.filter((id) => id !== tagId) : [...prev, tagId],
    );
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setIsSubmitting(true);
    setError(null);
    try {
      const post = await createPost({ title, body, tagIds: selectedTagIds });
      navigate(`/posts/${post.id}`);
    } catch (err) {
      setError(err instanceof Error ? err.message : String(err));
      setIsSubmitting(false);
    }
  };

  return (
    <Layout>
      <h1 className={h1Class}>投稿作成</h1>
      <form onSubmit={handleSubmit}>
        <div className={fieldClass}>
          <label htmlFor="title" className={labelClass}>
            タイトル
          </label>
          <input
            id="title"
            className={inputClass}
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
        </div>
        <div className={fieldClass}>
          <label htmlFor="body" className={labelClass}>
            本文
          </label>
          <textarea
            id="body"
            className={inputClass}
            value={body}
            onChange={(e) => setBody(e.target.value)}
            rows={8}
            required
          />
        </div>
        <div className={fieldClass}>
          <span className={labelClass}>タグ</span>
          {tags && tags.length === 0 ? (
            <p className="text-sm text-text">タグがありません</p>
          ) : (
            <div className="flex flex-wrap gap-3">
              {tags?.map((tag) => (
                <label key={tag.id} className={checkboxLabelClass}>
                  <input
                    type="checkbox"
                    checked={selectedTagIds.includes(tag.id)}
                    onChange={() => toggleTag(tag.id)}
                  />
                  {tag.name}
                </label>
              ))}
            </div>
          )}
        </div>
        {error && <p className={errorClass}>エラーが発生しました: {error}</p>}
        <Button type="submit" disabled={isSubmitting}>
          {isSubmitting ? '送信中...' : '投稿する'}
        </Button>
      </form>
    </Layout>
  );
}

export default PostFormPage;
