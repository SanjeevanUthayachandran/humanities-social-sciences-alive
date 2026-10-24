import { BookOpenCheck } from "lucide-react";
import BookCard from "@/components/readingclub/BookCard";
import Container from "@/components/common/Container";
import EmptyState from "@/components/common/EmptyState";
import PageBanner from "@/components/common/PageBanner";
import Reveal from "@/components/common/Reveal";
import SectionHeader from "@/components/common/SectionHeader";
import { useContent } from "@/hooks/useContent";

export default function ReadingClubPage() {
  const { books, isLoading, failedResources } = useContent();
  return (
    <>
      <PageBanner
        eyebrow="Reading Club"
        title="Read together. Think beyond the page."
        description="A shared catalogue for critical reading across literature, society, history, politics, and culture."
      />
      <section className="py-16 sm:py-20">
        <Container>
          <SectionHeader
            eyebrow="Book catalogue"
            title="Current reading-club selections."
            description="Books chosen for shared, critical reading."
            action={<BookOpenCheck className="size-8 text-emerald-600" />}
          />
          {books.length ? <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">{books.map((book, index) => <Reveal key={book.id} delay={index * 0.04}><BookCard book={book} /></Reveal>)}</div> : <EmptyState title={isLoading ? "Loading the reading list" : "No books available"} description={failedResources.includes("books") ? "The reading list could not be loaded. Please try again in a moment." : "The next reading-club selection will appear here once it is published."} />}
        </Container>
      </section>
    </>
  );
}
