import type { ReactNode } from 'react';
import { Link } from 'react-router-dom';

function Layout({ children }: { children: ReactNode }) {
  return (
    <div>
      <header className="bg-surface border-b border-border shadow-sm">
        <div className="max-w-360 mx-auto px-5 py-4">
          <Link
            to="/"
            className="inline-flex items-center gap-2 font-semibold text-lg text-text-h no-underline before:content-[''] before:w-2.5 before:h-2.5 before:rounded-[3px] before:bg-accent"
          >
            SpringLog
          </Link>
        </div>
      </header>
      <main className="max-w-360 mx-auto px-5 py-5">{children}</main>
    </div>
  );
}

export default Layout;
