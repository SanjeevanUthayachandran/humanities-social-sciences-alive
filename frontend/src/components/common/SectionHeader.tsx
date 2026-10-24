import type { ReactNode } from "react";
import { cn } from "@/lib/utils";

interface SectionHeaderProps {
  eyebrow?: string;
  title: string;
  description?: string;
  action?: ReactNode;
  align?: "left" | "center";
  className?: string;
}

export default function SectionHeader({
  eyebrow,
  title,
  description,
  action,
  align = "left",
  className,
}: SectionHeaderProps) {
  return (
    <div
      className={cn(
        "mb-10 flex flex-col gap-5 md:flex-row md:items-end md:justify-between",
        align === "center" && "mx-auto max-w-3xl items-center text-center md:block",
        className,
      )}
    >
      <div className={cn("max-w-3xl", align === "center" && "mx-auto")}>
        {eyebrow ? <p className="mb-3 text-xs font-bold uppercase tracking-[0.18em] text-blue-600">{eyebrow}</p> : null}
        <h2 className="text-3xl font-semibold text-slate-950 sm:text-4xl">{title}</h2>
        {description ? (
          <p className="mt-4 text-base leading-7 text-slate-600 sm:text-lg">{description}</p>
        ) : null}
      </div>
      {action ? <div className="shrink-0">{action}</div> : null}
    </div>
  );
}
