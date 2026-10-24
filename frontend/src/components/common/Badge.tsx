import type { ReactNode } from "react";
import { cn } from "@/lib/utils";

interface BadgeProps {
  children: ReactNode;
  className?: string;
}

export default function Badge({ children, className }: BadgeProps) {
  return (
    <span
      className={cn(
        "inline-flex items-center rounded-full bg-blue-100 px-3 py-1 text-[0.7rem] font-bold uppercase tracking-[0.12em] text-blue-700",
        className,
      )}
    >
      {children}
    </span>
  );
}
