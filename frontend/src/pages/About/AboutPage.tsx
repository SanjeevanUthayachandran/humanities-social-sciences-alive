import { ArrowRight, BookOpen, Briefcase, Compass, GraduationCap, HeartHandshake, Mic, Quote, Users } from "lucide-react";
import { Link } from "react-router-dom";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import PurposeCards from "@/components/common/PurposeCards";
import Reveal from "@/components/common/Reveal";
import { buttonVariants } from "@/components/ui/buttonVariants";

const OFFERINGS = [
  { icon: GraduationCap, title: "Courses", text: "English language, English for each discipline, and subjects taught in English from Grade 6 to A Levels." },
  { icon: Users, title: "Workshops", text: "Career guidance, research, teacher-training, and public-exam workshops." },
  { icon: BookOpen, title: "Reading club", text: "An online club for shared, critical reading of literature in English." },
  { icon: Compass, title: "Research", text: "Collective research and a course on the fundamentals of research." },
  { icon: Mic, title: "Guest lectures", text: "A monthly guest lecture to inform and empower the wider community." },
  { icon: Briefcase, title: "Internships & volunteering", text: "Opportunities for students to share what they know with their community." },
];

export default function AboutPage() {
  return (
    <>
      <PageBanner
        eyebrow="About HSSA"
        title="Humanities and Social Sciences, kept alive."
        description="An online platform for courses, workshops, collective research, and new initiatives that foster creativity and critical thinking."
      />

      <section className="py-14 sm:py-20">
        <Container>
          <div className="grid gap-10 lg:grid-cols-[0.8fr_1.2fr] lg:items-start">
            <Reveal>
              <p className="text-xs font-bold uppercase tracking-[0.18em] text-blue-600">Why we exist</p>
              <h2 className="mt-3 text-3xl font-semibold text-slate-950 sm:text-4xl">The disciplines that reveal injustice are the ones under threat.</h2>
            </Reveal>
            <Reveal delay={0.06}>
              <div className="space-y-5 text-base leading-8 text-slate-600 sm:text-lg">
                <p>
                  In a world caught up in a rat-race, paramount importance is given to the natural sciences, while the humanities and social sciences, which make people aware of anarchy, injustice, and inequality, are increasingly under threat.
                </p>
                <p>
                  Many humanities and social-science students come from socioeconomically disadvantaged backgrounds and study in Tamil or Sinhala. Their classmates in other faculties, taught in English, often reach jobs and higher study faster. HSSA helps arts learners close that gap by building subject knowledge in English, right after their A Levels.
                </p>
              </div>
            </Reveal>
          </div>
        </Container>
      </section>

      <section className="bg-blue-100 py-14 sm:py-20">
        <Container>
          <Reveal className="mx-auto max-w-2xl text-center">
            <p className="text-xs font-bold uppercase tracking-[0.18em] text-blue-700">Purpose, vision &amp; mission</p>
            <h2 className="mt-3 text-3xl font-semibold text-slate-950 sm:text-4xl">What guides everything we do.</h2>
          </Reveal>
          <PurposeCards className="mt-10" />
          <Reveal delay={0.1}>
            <blockquote className="mx-auto mt-10 flex max-w-2xl items-center justify-center gap-3 text-center">
              <Quote className="size-6 shrink-0 text-blue-700" aria-hidden="true" />
              <p className="font-heading text-2xl font-medium italic text-slate-950">What matters is what matters.</p>
            </blockquote>
          </Reveal>
        </Container>
      </section>

      <section className="py-14 sm:py-20">
        <Container>
          <Reveal className="max-w-2xl">
            <p className="text-xs font-bold uppercase tracking-[0.18em] text-blue-600">What we offer</p>
            <h2 className="mt-3 text-3xl font-semibold text-slate-950 sm:text-4xl">One connected path from curiosity to contribution.</h2>
          </Reveal>
          <div className="mt-10 grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
            {OFFERINGS.map(({ icon: Icon, title, text }, index) => (
              <Reveal key={title} delay={(index % 3) * 0.06} className="h-full">
                <article className="h-full rounded-2xl border border-slate-200 bg-white p-6 card-lift">
                  <span className="flex size-10 items-center justify-center rounded-xl bg-blue-100 text-blue-700"><Icon className="size-5" aria-hidden="true" /></span>
                  <h3 className="mt-4 text-lg font-semibold text-slate-950">{title}</h3>
                  <p className="mt-2 text-sm leading-6 text-slate-600">{text}</p>
                </article>
              </Reveal>
            ))}
          </div>
          <Reveal delay={0.1}>
            <Link to="/courses" className={buttonVariants({ size: "lg", className: "mt-8 bg-blue-600 text-white hover:bg-blue-700" })}>Explore the courses <ArrowRight /></Link>
          </Reveal>
        </Container>
      </section>

      <section className="bg-emerald-600 py-14 text-white sm:py-16">
        <Container>
          <Reveal className="grid gap-6 lg:grid-cols-[auto_1fr] lg:items-center lg:gap-10">
            <span className="flex size-14 items-center justify-center rounded-2xl bg-white/15"><HeartHandshake className="size-7" aria-hidden="true" /></span>
            <div className="max-w-3xl">
              <h2 className="text-2xl font-semibold sm:text-3xl">Affordable by design.</h2>
              <p className="mt-3 text-base leading-7 text-white/85 sm:text-lg">
                We charge very little, considering our learners&rsquo; socioeconomic backgrounds, and half of the money is used for charity work.
              </p>
            </div>
          </Reveal>
        </Container>
      </section>

      <section className="py-14 sm:py-20">
        <Container>
          <Reveal className="max-w-3xl">
            <p className="text-xs font-bold uppercase tracking-[0.18em] text-blue-600">Founder</p>
            <h2 className="mt-3 text-3xl font-semibold text-slate-950 sm:text-4xl">Gunasingarajah Maheshwaralingam</h2>
            <p className="mt-2 text-sm font-semibold text-slate-500">Academic staff member, University of Peradeniya</p>
            <p className="mt-5 text-base leading-8 text-slate-600 sm:text-lg">
              Passionate about the humanities and social sciences, and about the tension between idealism and realism, he founded HSSA to make quality, English-medium learning reachable for arts students.
            </p>
          </Reveal>
        </Container>
      </section>
    </>
  );
}
