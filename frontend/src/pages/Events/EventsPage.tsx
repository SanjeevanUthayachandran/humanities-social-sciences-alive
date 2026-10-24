import EmptyState from "@/components/common/EmptyState";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import Reveal from "@/components/common/Reveal";
import SectionHeader from "@/components/common/SectionHeader";
import EventCard from "@/components/events/EventCard";
import { useContent } from "@/hooks/useContent";

export default function EventsPage() {
  const { events, isLoading, failedResources } = useContent();
  return (
    <>
      <PageBanner
        eyebrow="Events"
        title="Gather around ideas that matter."
        description="Explore reading evenings, open days, guest conversations, and public academic programs published by the HSSA team."
      />

      <section className="py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Upcoming events"
            title="Public programs on the calendar."
            description="Reading evenings, open days, and guest conversations open to the community."
          />
          {events.length ? <div className="grid gap-5 lg:grid-cols-2">
            {events.map((event, index) => (
              <Reveal key={event.id} delay={index * 0.05}>
                <EventCard event={event} />
              </Reveal>
            ))}
          </div> : <EmptyState title={isLoading ? "Loading events" : "No events scheduled"} description={failedResources.includes("events") ? "Events could not be loaded. Please try again in a moment." : "New events will appear here once they are published."} />}
        </Container>
      </section>
    </>
  );
}
