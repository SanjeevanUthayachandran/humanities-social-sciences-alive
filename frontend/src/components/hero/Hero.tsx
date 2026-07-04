import { ArrowRight, BookOpen, GraduationCap, UsersRound } from "lucide-react";
import { motion } from "framer-motion";
import { Link } from "react-router-dom";
import brandImage from "@/assets/hssa-brand.png";
import platformGraphic from "@/assets/hero.png";
import Container from "@/components/common/Container";
import { buttonVariants } from "@/components/ui/buttonVariants";

const metrics = [
  { label: "Course pathways", value: "20+" },
  { label: "Research streams", value: "4" },
  { label: "Public programs", value: "8" },
];

export default function Hero() {
  return (
    <section className="overflow-hidden bg-gradient-to-br from-white via-blue-50/70 to-emerald-50/60 py-16 sm:py-20 lg:py-24">
      <Container>
        <div className="grid items-center gap-12 lg:grid-cols-[1.05fr_0.95fr]">
          <motion.div
            initial={{ opacity: 0, y: 22 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, ease: "easeOut" }}
          >
            <p className="inline-flex items-center rounded-md border border-blue-100 bg-white px-3 py-1 text-sm font-semibold text-blue-700 shadow-sm">
              <GraduationCap className="mr-2 size-4" />
              Learn, research, think, transform
            </p>
            <h1 className="mt-6 max-w-4xl text-4xl font-bold tracking-normal text-slate-950 sm:text-5xl lg:text-6xl">
              Humanities & Social Sciences Alive
            </h1>
            <p className="mt-6 max-w-2xl text-lg leading-8 text-slate-600 sm:text-xl">
              A premium digital education and research platform for critical thinking,
              language learning, public dialogue, workshops, and humanities-led social awareness.
            </p>
            <div className="mt-8 flex flex-col gap-3 sm:flex-row">
              <Link to="/courses" className={buttonVariants({ size: "lg", className: "bg-blue-600 hover:bg-blue-700" })}>
                Explore Courses
                <ArrowRight />
              </Link>
              <Link to="/research" className={buttonVariants({ variant: "outline", size: "lg" })}>
                View Research
              </Link>
            </div>
            <dl className="mt-10 grid max-w-2xl grid-cols-3 gap-3">
              {metrics.map((metric) => (
                <div key={metric.label} className="rounded-lg border border-white bg-white/80 p-4 shadow-sm">
                  <dt className="text-xs font-semibold uppercase tracking-[0.12em] text-slate-500">
                    {metric.label}
                  </dt>
                  <dd className="mt-2 text-2xl font-bold text-slate-950">{metric.value}</dd>
                </div>
              ))}
            </dl>
          </motion.div>

          <motion.div
            className="relative"
            initial={{ opacity: 0, scale: 0.96 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.65, ease: "easeOut", delay: 0.1 }}
          >
            <div className="rounded-lg border border-slate-200 bg-white p-4 shadow-2xl">
              <img
                src={brandImage}
                alt="Humanities & Social Sciences Alive brand"
                className="h-auto w-full rounded-md border border-slate-100 object-cover"
              />
            </div>
            <div className="mt-4 grid gap-4 sm:grid-cols-2">
              <div className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
                <BookOpen className="size-6 text-blue-600" />
                <p className="mt-3 text-sm font-semibold text-slate-950">Courses for school, university, and lifelong learning.</p>
              </div>
              <div className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
                <UsersRound className="size-6 text-emerald-600" />
                <p className="mt-3 text-sm font-semibold text-slate-950">Research, workshops, reading clubs, and public programs.</p>
              </div>
            </div>
            <img
              src={platformGraphic}
              alt=""
              className="pointer-events-none absolute -right-8 -top-8 hidden w-24 opacity-70 sm:block"
            />
          </motion.div>
        </div>
      </Container>
    </section>
  );
}
