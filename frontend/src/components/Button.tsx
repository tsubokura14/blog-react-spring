import type { ButtonHTMLAttributes } from 'react';

export const buttonClass =
  'inline-flex items-center rounded-md px-4 py-2 my-4 text-[15px] font-medium ' +
  'text-accent-contrast bg-accent hover:bg-accent-hover hover:text-accent-contrast ' +
  'active:translate-y-px cursor-pointer no-underline transition-[background-color,transform] ' +
  'focus-visible:outline focus-visible:outline-2 focus-visible:outline-accent focus-visible:outline-offset-2 ' +
  'disabled:opacity-60 disabled:cursor-not-allowed';

function Button({ className, ...rest }: ButtonHTMLAttributes<HTMLButtonElement>) {
  return <button className={[buttonClass, className].filter(Boolean).join(' ')} {...rest} />;
}

export default Button;
