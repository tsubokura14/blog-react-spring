import { useEffect, useState } from 'react';

type AsyncState<T> = {
  data: T | null;
  isLoading: boolean;
  error: string | null;
};

// fetch → loading/error/dataの状態管理パターンを共通化するフック。
// depsが変わるたびfnを再実行し、アンマウント/再実行後は古い結果でsetStateしない
export function useAsync<T>(fn: () => Promise<T>, deps: unknown[]): AsyncState<T> {
  const [state, setState] = useState<AsyncState<T>>({
    data: null,
    isLoading: true,
    error: null,
  });

  useEffect(() => {
    let cancelled = false;
    // depsが変わるたびに再フェッチを開始したことを示すためのリセット。
    // react.devのデータ取得Effect例と同じ形だが、react-hooks/set-state-in-effectが
    // Effect本体での同期setStateを一律で警告するためこの行だけ無効化する
    // eslint-disable-next-line react-hooks/set-state-in-effect
    setState({ data: null, isLoading: true, error: null });

    fn()
      .then((data) => {
        if (!cancelled) {
          setState({ data, isLoading: false, error: null });
        }
      })
      .catch((err: unknown) => {
        if (!cancelled) {
          setState({
            data: null,
            isLoading: false,
            error: err instanceof Error ? err.message : String(err),
          });
        }
      });

    return () => {
      cancelled = true;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, deps);

  return state;
}
