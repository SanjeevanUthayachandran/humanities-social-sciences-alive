import type { ReactNode } from "react";
import Container from "@/components/common/Container";
import Reveal from "@/components/common/Reveal";

interface PageBannerProps {
  eyebrow: string;
  title: string;
  description: string;
  children?: ReactNode;
}

export default function PageBanner({ eyebrow, title, description, children }: PageBannerProps) {
  return (
    <section className="bg-blue-100 py-14 sm:py-20">
      <Container>
        <Reveal className="max-w-4xl">
          <p className="text-xs font-bold uppercase tracking-[0.18em] text-blue-700">{eyebrow}</p>
          <h1 className="mt-4 text-4xl font-semibold text-slate-950 sm:text-5xl">
            {title}
          </h1>
          <p className="mt-5 max-w-3xl text-lg leading-8 text-slate-700">{description}</p>
          {children ? <div className="mt-8">{children}</div> : null}
        </Reveal>
      </Container>
    </section>
  );
}
