import { useState } from "react";
import Container from "@/components/common/Container";
import PageBanner from "@/components/common/PageBanner";
import Reveal from "@/components/common/Reveal";
import SectionHeader from "@/components/common/SectionHeader";
import WorkshopCard from "@/components/workshops/WorkshopCard";
import { Button } from "@/components/ui/button";
import { workshops } from "@/services/contentService";
import type { Workshop } from "@/types/content";

type WorkshopStatus = Workshop["status"] | "All";

const filters: WorkshopStatus[] = ["Upcoming", "Past", "All"];

export default function WorkshopsPage() {
  const [status, setStatus] = useState<WorkshopStatus>("Upcoming");
  const visibleWorkshops = workshops.filter((workshop) => status === "All" || workshop.status === status);

  return (
    <>
      <PageBanner
        eyebrow="Workshops"
        title="Live workshops for practical academic and professional development."
        description="Registration buttons are UI-only for now and ready for enrollment, email, and payment workflows in future phases."
      />

      <section className="py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Program calendar"
            title="Upcoming and past workshops."
            description="Switch between scheduled sessions and completed workshops to see how public programs can be managed from mock data."
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
          <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
            {visibleWorkshops.map((workshop, index) => (
              <Reveal key={workshop.id} delay={index * 0.05}>
                <WorkshopCard workshop={workshop} />
              </Reveal>
            ))}
          </div>
        </Container>
      </section>
    </>
  );
}
