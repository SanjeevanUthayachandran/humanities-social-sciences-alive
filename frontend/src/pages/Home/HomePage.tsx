import { ArrowRight, CalendarDays } from "lucide-react";
import { Link } from "react-router-dom";
import EmptyState from "@/components/common/EmptyState";
import PurposeCards from "@/components/common/PurposeCards";
import Container from "@/components/common/Container";
import Reveal from "@/components/common/Reveal";
import SectionHeader from "@/components/common/SectionHeader";
import CourseCard from "@/components/courses/CourseCard";
import EventCard from "@/components/events/EventCard";
import Hero from "@/components/hero/Hero";
import ResearchCard from "@/components/research/ResearchCard";
import WorkshopCard from "@/components/workshops/WorkshopCard";
import { buttonVariants } from "@/components/ui/buttonVariants";
import { useContent } from "@/hooks/useContent";

function CollectionEmpty({ name, loading, unavailable }: { name: string; loading: boolean; unavailable: boolean }) {
  if (loading) return <EmptyState title={`Loading ${name}`} description="Please wait a moment." />;
  if (unavailable) return <EmptyState title={`${name[0].toUpperCase()}${name.slice(1)} unavailable`} description="We could not load this right now. Please refresh the page in a moment." />;
  return <EmptyState title={`No ${name} published yet`} description={`New ${name} will appear here as soon as the HSSA team publishes them.`} />;
}

export default function HomePage() {
  const { courses, researchItems, workshops, events, isLoading, failedResources } = useContent();
  const featuredCourses = courses.slice(0, 3);
  const latestResearch = researchItems.slice(0, 2);
  const upcomingWorkshops = workshops.filter((workshop) => workshop.status === "Upcoming").slice(0, 3);
  const upcomingEvents = events.slice(0, 2);

  return (
    <>
      <Hero />

      <section className="bg-blue-100 py-14 sm:py-20">
        <Container>
          <Reveal className="mx-auto max-w-3xl text-center">
            <p className="text-xs font-bold uppercase tracking-[0.18em] text-blue-700">Who we are</p>
            <h2 className="mt-3 text-3xl font-semibold text-slate-950 sm:text-4xl">What matters is what matters.</h2>
            <p className="mt-4 text-base leading-7 text-slate-700 sm:text-lg sm:leading-8">
              HSSA helps learners moving from Tamil- or Sinhala-medium education into English-language higher study, and keeps critical thinking at the centre of it all.
            </p>
          </Reveal>
          <PurposeCards className="mt-10" />
          <div className="mt-8 text-center">
            <Link to="/about" className={buttonVariants({ variant: "outline", className: "border-slate-900/25" })}>Read our story <ArrowRight /></Link>
          </div>
        </Container>
      </section>

      <section className="bg-slate-100/70 py-14 sm:py-20">
        <Container>
          <SectionHeader eyebrow="Featured courses" title="Start with the skills your discipline demands." description="Explore the courses currently open at HSSA." action={<Link to="/courses" className={buttonVariants({ variant: "outline" })}>All Courses <ArrowRight /></Link>} />
          {featuredCourses.length ? <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">{featuredCourses.map((course, index) => <Reveal key={course.id} delay={index * 0.06}><CourseCard course={course} /></Reveal>)}</div> : <CollectionEmpty name="courses" loading={isLoading} unavailable={failedResources.includes("courses")} />}
        </Container>
      </section>

      <section className="py-14 sm:py-20">
        <Container>
          <SectionHeader eyebrow="Research" title="Questions worth investigating." description="Read the latest research projects published by HSSA." action={<Link to="/research" className={buttonVariants({ variant: "outline" })}>Research Hub <ArrowRight /></Link>} />
          {latestResearch.length ? <div className="grid gap-5 lg:grid-cols-2">{latestResearch.map((item, index) => <Reveal key={item.id} delay={index * 0.06}><ResearchCard item={item} /></Reveal>)}</div> : <CollectionEmpty name="research projects" loading={isLoading} unavailable={failedResources.includes("research")} />}
        </Container>
      </section>

      <section className="bg-slate-100/70 py-14 sm:py-20">
        <Container>
          <SectionHeader eyebrow="Live learning" title="Workshops that turn knowledge into practice." description="Join upcoming sessions and register with your HSSA account." />
          {upcomingWorkshops.length ? <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">{upcomingWorkshops.map((workshop, index) => <Reveal key={workshop.id} delay={index * 0.06}><WorkshopCard workshop={workshop} /></Reveal>)}</div> : <CollectionEmpty name="upcoming workshops" loading={isLoading} unavailable={failedResources.includes("workshops")} />}
          <Link to="/workshops" className={buttonVariants({ className: "mt-8 bg-blue-600 text-white hover:bg-blue-700" })}>View Workshops <ArrowRight /></Link>
        </Container>
      </section>

      <section className="py-14 sm:py-20">
        <Container>
          <div className="grid gap-8 lg:grid-cols-[0.72fr_1.28fr] lg:items-start">
            <SectionHeader eyebrow="Events" title="Meet, listen, question, and contribute." description="Join public conversations, reading sessions, open days, and academic community events." action={<Link to="/events" className={buttonVariants({ variant: "outline" })}>Event Calendar <CalendarDays /></Link>} className="mb-0" />
            {upcomingEvents.length ? <div className="grid gap-5">{upcomingEvents.map((event, index) => <Reveal key={event.id} delay={index * 0.06}><EventCard event={event} /></Reveal>)}</div> : <CollectionEmpty name="events" loading={isLoading} unavailable={failedResources.includes("events")} />}
          </div>
        </Container>
      </section>
    </>
  );
}
