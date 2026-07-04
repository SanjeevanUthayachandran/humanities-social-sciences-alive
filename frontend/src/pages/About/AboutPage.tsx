import { CheckCircle2, Quote, UserRound } from "lucide-react";
import Timeline from "@/components/about/Timeline";
import CtaSection from "@/components/common/CtaSection";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import Reveal from "@/components/common/Reveal";
import SectionHeader from "@/components/common/SectionHeader";
import { about } from "@/services/contentService";

export default function AboutPage() {
  return (
    <>
      <PageBanner
        eyebrow="About HSSA"
        title="A platform for learning, research, and critical public life."
        description={about.intro}
      />

      <section className="py-16 sm:py-20">
        <Container>
          <div className="grid gap-5 lg:grid-cols-2">
            <Reveal>
              <article className="h-full rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
                <span className="text-sm font-bold uppercase tracking-[0.16em] text-blue-700">Mission</span>
                <h2 className="mt-4 text-2xl font-bold text-slate-950">Doing the right thing in pursuit of social justice.</h2>
                <p className="mt-4 leading-7 text-slate-600">{about.mission}</p>
              </article>
            </Reveal>
            <Reveal delay={0.08}>
              <article className="h-full rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
                <span className="text-sm font-bold uppercase tracking-[0.16em] text-emerald-700">Vision</span>
                <h2 className="mt-4 text-2xl font-bold text-slate-950">Creativity and critique for the real world.</h2>
                <p className="mt-4 leading-7 text-slate-600">{about.vision}</p>
              </article>
            </Reveal>
          </div>
        </Container>
      </section>

      <section className="bg-slate-50 py-16 sm:py-20">
        <Container>
          <div className="grid gap-8 lg:grid-cols-[0.9fr_1.1fr] lg:items-start">
            <Reveal>
              <SectionHeader
                eyebrow="Founder"
                title="Founder profile integrated into the platform story."
                description="The About page presents the founder as part of the institution's intellectual direction, not as a separate vanity page."
                className="mb-0"
              />
            </Reveal>
            <Reveal delay={0.08}>
              <article className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
                <div className="flex items-start gap-4">
                  <div className="grid size-14 shrink-0 place-items-center rounded-lg bg-blue-50 text-blue-700">
                    <UserRound className="size-7" />
                  </div>
                  <div>
                    <h2 className="text-2xl font-bold text-slate-950">{about.founder.name}</h2>
                    <p className="mt-1 text-sm font-semibold text-blue-700">{about.founder.role}</p>
                  </div>
                </div>
                <p className="mt-6 leading-7 text-slate-600">{about.founder.summary}</p>
                <div className="mt-6 flex flex-wrap gap-2">
                  {about.founder.interests.map((interest) => (
                    <span key={interest} className="rounded-md bg-slate-100 px-3 py-1.5 text-sm font-medium text-slate-700">
                      {interest}
                    </span>
                  ))}
                </div>
              </article>
            </Reveal>
          </div>
        </Container>
      </section>

      <section className="py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Objectives"
            title="What the platform is built to support."
            description="The public website is the first stage of a larger education, research, publication, and learning management ecosystem."
          />
          <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
            {about.objectives.map((objective, index) => (
              <Reveal key={objective} delay={index * 0.05}>
                <article className="flex h-full gap-3 rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
                  <CheckCircle2 className="mt-0.5 size-5 shrink-0 text-emerald-600" />
                  <p className="text-sm leading-6 text-slate-700">{objective}</p>
                </article>
              </Reveal>
            ))}
          </div>
        </Container>
      </section>

      <section className="bg-slate-50 py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Timeline"
            title="A staged path toward an enterprise-grade education platform."
            description="The roadmap starts with a polished public website and grows toward authenticated learning, research publishing, analytics, and cloud deployment."
          />
          <Timeline items={about.timeline} />
        </Container>
      </section>

      <section className="py-16 sm:py-20">
        <Container>
          <Reveal>
            <div className="rounded-lg border border-blue-100 bg-blue-50 p-8 text-center">
              <Quote className="mx-auto size-9 text-blue-700" />
              <p className="mx-auto mt-4 max-w-2xl text-2xl font-bold leading-10 text-slate-950">
                {about.quote}
              </p>
            </div>
          </Reveal>
        </Container>
      </section>

      <CtaSection
        title="Turn curiosity into a structured learning path."
        description="Start with the course catalogue, then move into research, workshops, reading programs, and public events."
      />
    </>
  );
}
