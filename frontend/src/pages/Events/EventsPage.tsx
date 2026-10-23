import { CalendarDays, Image, MapPin } from "lucide-react";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import Reveal from "@/components/common/Reveal";
import SectionHeader from "@/components/common/SectionHeader";
import EventCard from "@/components/events/EventCard";
import { useContent } from "@/hooks/useContent";
import { formatDate, formatMonth } from "@/utils/format";

export default function EventsPage() {
  const { events } = useContent();
  const groupedEvents = events.reduce<Record<string, typeof events>>((groups, event) => {
    const month = formatMonth(event.date);
    return { ...groups, [month]: [...(groups[month] ?? []), event] };
  }, {});

  return (
    <>
      <PageBanner
        eyebrow="Events"
        title="Events, reading evenings, launch previews, and public academic programs."
        description="A responsive event page with calendar grouping, event cards, and gallery placeholders for future media uploads."
      />

      <section className="py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Upcoming events"
            title="Public programs on the calendar."
            description="Events are loaded from the public program API with dates, locations, types, and summaries."
          />
          <div className="grid gap-5 lg:grid-cols-2">
            {events.map((event, index) => (
              <Reveal key={event.id} delay={index * 0.05}>
                <EventCard event={event} />
              </Reveal>
            ))}
          </div>
        </Container>
      </section>

      <section className="bg-slate-50 py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Calendar"
            title="Month-by-month layout."
            description="The calendar stays synchronized with the event management module."
          />
          <div className="grid gap-6">
            {Object.entries(groupedEvents).map(([month, monthEvents]) => (
              <article key={month} className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
                <div className="flex items-center gap-3">
                  <CalendarDays className="size-6 text-blue-600" />
                  <h2 className="text-xl font-bold text-slate-950">{month}</h2>
                </div>
                <div className="mt-5 grid gap-4">
                  {monthEvents.map((event) => (
                    <div key={event.id} className="grid gap-3 rounded-lg bg-slate-50 p-4 sm:grid-cols-[150px_1fr]">
                      <div className="text-sm font-bold text-blue-700">{formatDate(event.date)}</div>
                      <div>
                        <h3 className="font-bold text-slate-950">{event.title}</h3>
                        <p className="mt-1 flex items-center gap-2 text-sm text-slate-600">
                          <MapPin className="size-4 text-emerald-600" />
                          {event.location} at {event.time}
                        </p>
                      </div>
                    </div>
                  ))}
                </div>
              </article>
            ))}
          </div>
        </Container>
      </section>

      <section className="py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Gallery"
            title="Media gallery preview."
            description="Image placeholders show how future event photos and workshop media can be displayed without changing the page structure."
          />
          <div className="grid gap-4 md:grid-cols-3">
            {["Launch preview", "Reading club", "Research colloquium"].map((item, index) => (
              <div key={item} className="aspect-[4/3] rounded-lg border border-slate-200 bg-gradient-to-br from-blue-50 to-emerald-50 p-5 shadow-sm">
                <Image className="size-8 text-blue-600" />
                <p className="mt-5 text-sm font-semibold uppercase tracking-[0.14em] text-slate-500">
                  Gallery {index + 1}
                </p>
                <h3 className="mt-2 text-xl font-bold text-slate-950">{item}</h3>
              </div>
            ))}
          </div>
        </Container>
      </section>
    </>
  );
}
