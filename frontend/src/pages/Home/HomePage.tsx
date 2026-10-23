import { ArrowRight, CalendarDays, Lightbulb, Newspaper, UsersRound } from "lucide-react";
import { Link } from "react-router-dom";
import CourseCard from "@/components/courses/CourseCard";
import CtaSection from "@/components/common/CtaSection";
import Container from "@/components/common/Container";
import Reveal from "@/components/common/Reveal";
import SectionHeader from "@/components/common/SectionHeader";
import EventCard from "@/components/events/EventCard";
import Hero from "@/components/hero/Hero";
import NewsCard from "@/components/news/NewsCard";
import ResearchCard from "@/components/research/ResearchCard";
import WorkshopCard from "@/components/workshops/WorkshopCard";
import { buttonVariants } from "@/components/ui/buttonVariants";
import { useContent } from "@/hooks/useContent";
import { about, getLatestNews } from "@/services/contentService";

export default function HomePage() {
  const { courses, researchItems, workshops, events } = useContent();
  const featuredCourses = courses.filter((course) => course.featured).slice(0, 3);
  const featuredResearch = researchItems.filter((item) => item.featured).slice(0, 2);
  const upcomingWorkshops = workshops.filter((workshop) => workshop.status === "Upcoming").slice(0, 3);
  const upcomingEvents = events.slice(0, 2);
  const latestNews = getLatestNews().slice(0, 3);

  return (
    <>
      <Hero />

      <section className="py-16 sm:py-20">
        <Container>
          <div className="grid gap-8 lg:grid-cols-[0.85fr_1.15fr] lg:items-center">
            <Reveal>
              <SectionHeader
                eyebrow="About the platform"
                title="A serious digital home for learning, research, and public thought."
                description={about.intro}
                className="mb-0"
              />
              <Link to="/about" className={buttonVariants({ className: "mt-6 bg-blue-600 hover:bg-blue-700" })}>
                Learn About HSSA
                <ArrowRight />
              </Link>
            </Reveal>
            <div className="grid gap-4 sm:grid-cols-3">
              {[
                { title: "Mission", text: about.mission, icon: Lightbulb },
                { title: "Vision", text: about.vision, icon: UsersRound },
                { title: "Research", text: "Courses, publications, reading clubs, and collaborative inquiry for future platform phases.", icon: Newspaper },
              ].map(({ title, text, icon: Icon }, index) => (
                <Reveal key={title} delay={index * 0.06}>
                  <article className="h-full rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
                    <Icon className="size-6 text-blue-600" />
                    <h3 className="mt-4 font-bold text-slate-950">{title}</h3>
                    <p className="mt-2 text-sm leading-6 text-slate-600">{text}</p>
                  </article>
                </Reveal>
              ))}
            </div>
          </div>
        </Container>
      </section>

      <section className="bg-slate-50 py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Featured courses"
            title="Structured pathways for language, research, and public communication."
            description="Explore practical courses that prepare learners for academic study, public life, and professional communication."
            action={
              <Link to="/courses" className={buttonVariants({ variant: "outline" })}>
                All Courses
                <ArrowRight />
              </Link>
            }
          />
          <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
            {featuredCourses.map((course, index) => (
              <Reveal key={course.id} delay={index * 0.06}>
                <CourseCard course={course} />
              </Reveal>
            ))}
          </div>
        </Container>
      </section>

      <section className="py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Research preview"
            title="Research work designed for future publication workflows."
            description="The research area prepares public briefs, reading lists, downloadable resources, and collaborative humanities and social sciences projects."
            action={
              <Link to="/research" className={buttonVariants({ variant: "outline" })}>
                Research Hub
                <ArrowRight />
              </Link>
            }
          />
          <div className="grid gap-5 lg:grid-cols-2">
            {featuredResearch.map((item, index) => (
              <Reveal key={item.id} delay={index * 0.06}>
                <ResearchCard item={item} />
              </Reveal>
            ))}
          </div>
        </Container>
      </section>

      <section className="bg-slate-50 py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Workshops"
            title="Live programs for practical academic and professional skills."
            description="Upcoming sessions focus on research proposals, speaking, critical reading, and soft-skill development."
          />
          <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
            {upcomingWorkshops.map((workshop, index) => (
              <Reveal key={workshop.id} delay={index * 0.06}>
                <WorkshopCard workshop={workshop} />
              </Reveal>
            ))}
          </div>
        </Container>
      </section>

      <section className="py-16 sm:py-20">
        <Container>
          <div className="grid gap-8 lg:grid-cols-[0.85fr_1.15fr] lg:items-start">
            <SectionHeader
              eyebrow="Events"
              title="Public programs and academic community moments."
              description="Join previews, reading evenings, research colloquia, and language learning open days."
              action={
                <Link to="/events" className={buttonVariants({ variant: "outline" })}>
                  Event Calendar
                  <CalendarDays />
                </Link>
              }
              className="mb-0"
            />
            <div className="grid gap-5">
              {upcomingEvents.map((event, index) => (
                <Reveal key={event.id} delay={index * 0.06}>
                  <EventCard event={event} />
                </Reveal>
              ))}
            </div>
          </div>
        </Container>
      </section>

      <section className="bg-slate-50 py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Latest news"
            title="Announcements from the platform."
            description="Follow public development updates, course launches, research desk plans, and workshop previews."
            action={
              <Link to="/news" className={buttonVariants({ variant: "outline" })}>
                Newsroom
                <ArrowRight />
              </Link>
            }
          />
          <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
            {latestNews.map((item, index) => (
              <Reveal key={item.id} delay={index * 0.06}>
                <NewsCard item={item} />
              </Reveal>
            ))}
          </div>
        </Container>
      </section>

      <CtaSection />
    </>
  );
}
