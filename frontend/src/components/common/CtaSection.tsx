import { ArrowRight, Mail } from "lucide-react";
import { Link } from "react-router-dom";
import Container from "@/components/common/Container";
import { buttonVariants } from "@/components/ui/buttonVariants";

interface CtaSectionProps {
  title?: string;
  description?: string;
  primaryLabel?: string;
  primaryPath?: string;
  secondaryLabel?: string;
  secondaryPath?: string;
}

export default function CtaSection({
  title = "Build critical thinking into the way you learn.",
  description = "Explore courses, research, workshops, and public events designed for serious learners and future contributors.",
  primaryLabel = "Explore Courses",
  primaryPath = "/courses",
  secondaryLabel = "Contact Us",
  secondaryPath = "/contact",
}: CtaSectionProps) {
  return (
    <section className="bg-slate-950 py-16 text-white sm:py-20">
      <Container>
        <div className="grid gap-8 lg:grid-cols-[1fr_auto] lg:items-center">
          <div className="max-w-3xl">
            <p className="text-sm font-semibold uppercase tracking-[0.18em] text-emerald-300">
              Humanities & Social Sciences Alive
            </p>
            <h2 className="mt-4 text-3xl font-bold tracking-normal sm:text-4xl">{title}</h2>
            <p className="mt-4 text-lg leading-8 text-slate-300">{description}</p>
          </div>
          <div className="flex flex-col gap-3 sm:flex-row lg:flex-col xl:flex-row">
            <Link to={primaryPath} className={buttonVariants({ size: "lg", className: "bg-blue-600 hover:bg-blue-700" })}>
              {primaryLabel}
              <ArrowRight />
            </Link>
            <Link to={secondaryPath} className={buttonVariants({ variant: "outline", size: "lg", className: "border-white/25 bg-white/10 text-white hover:bg-white/15 hover:text-white" })}>
              <Mail />
              {secondaryLabel}
            </Link>
          </div>
        </div>
      </Container>
    </section>
  );
}
