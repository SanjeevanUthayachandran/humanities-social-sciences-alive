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
    <section className="bg-slate-950 py-14 text-white sm:py-16">
      <Container>
        <div className="grid gap-8 lg:grid-cols-[1fr_auto] lg:items-center">
          <div className="max-w-3xl">
            <p className="text-xs font-bold uppercase tracking-[0.18em] text-blue-300">
              Humanities & Social Sciences Alive
            </p>
            <h2 className="mt-4 text-3xl font-semibold sm:text-4xl">{title}</h2>
            <p className="mt-4 text-base leading-7 text-slate-300 sm:text-lg">{description}</p>
          </div>
          <div className="flex flex-col gap-3 sm:flex-row lg:flex-col xl:flex-row">
            <Link to={primaryPath} className={buttonVariants({ size: "lg", className: "bg-blue-600 text-white hover:bg-blue-500" })}>
              {primaryLabel}
              <ArrowRight />
            </Link>
            <Link to={secondaryPath} className={buttonVariants({ variant: "outline", size: "lg", className: "border-white/30 bg-transparent text-white hover:bg-white/10 hover:text-white" })}>
              <Mail />
              {secondaryLabel}
            </Link>
          </div>
        </div>
      </Container>
    </section>
  );
}
