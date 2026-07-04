import Container from "@/components/common/Container";
import { CardSkeleton, Skeleton } from "@/components/common/Skeleton";

export default function PageLoader() {
  return (
    <section className="py-16 sm:py-20">
      <Container>
        <Skeleton className="h-5 w-32" />
        <Skeleton className="mt-5 h-10 w-full max-w-2xl" />
        <Skeleton className="mt-4 h-5 w-full max-w-3xl" />
        <div className="mt-10 grid gap-5 md:grid-cols-2 xl:grid-cols-3">
          <CardSkeleton />
          <CardSkeleton />
          <CardSkeleton />
        </div>
      </Container>
    </section>
  );
}
