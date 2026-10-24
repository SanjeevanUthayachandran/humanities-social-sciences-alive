import { useState } from "react";
import Container from "@/components/common/Container";
import EmptyState from "@/components/common/EmptyState";
import PageBanner from "@/components/common/PageBanner";
import Reveal from "@/components/common/Reveal";
import SectionHeader from "@/components/common/SectionHeader";
import WorkshopCard from "@/components/workshops/WorkshopCard";
import { Button } from "@/components/ui/button";
import { useContent } from "@/hooks/useContent";
import type { Workshop } from "@/types/content";

type WorkshopStatus = Workshop["status"] | "All";

const filters: WorkshopStatus[] = ["Upcoming", "Past", "All"];

export default function WorkshopsPage() {
  const { workshops, isLoading, failedResources } = useContent();
  const [status, setStatus] = useState<WorkshopStatus>("Upcoming");
  const visibleWorkshops = workshops.filter((workshop) => status === "All" || workshop.status === status);

  return (
    <>
      <PageBanner
        eyebrow="Workshops"
        title="Live workshops for practical academic and professional development."
        description="Browse upcoming and past workshops and register with your HSSA account."
      />

      <section className="py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Program calendar"
            title="Upcoming and past workshops."
            description="Switch between scheduled sessions and completed workshops."
          />
          <div className="mb-8 inline-flex rounded-lg border border-slate-200 bg-white p-1 shadow-sm">
            {filters.map((filter) => (
              <Button
                key={filter}
                type="button"
                variant={status === filter ? "default" : "ghost"}
                onClick={() => setStatus(filter)}
                className={status === filter ? "bg-blue-600 hover:bg-blue-700" : ""}
              >
                {filter}
              </Button>
            ))}
          </div>
          {visibleWorkshops.length ? <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
            {visibleWorkshops.map((workshop, index) => (
              <Reveal key={workshop.id} delay={index * 0.05}>
                <WorkshopCard workshop={workshop} />
              </Reveal>
            ))}
          </div> : <EmptyState title={isLoading ? "Loading workshops" : "No workshops found"} description={failedResources.includes("workshops") ? "Workshops could not be loaded. Please try again in a moment." : "Try another filter or check back for newly published workshops."} />}
        </Container>
      </section>
    </>
  );
}
