import { ArrowLeft, Search } from "lucide-react";
import { Link } from "react-router-dom";
import Container from "@/components/common/Container";
import { buttonVariants } from "@/components/ui/buttonVariants";

export default function NotFoundPage() {
  return (
    <section className="py-20 sm:py-28">
      <Container className="text-center">
        <p className="text-sm font-bold uppercase tracking-[0.18em] text-blue-700">404</p>
        <h1 className="mt-4 text-4xl font-bold text-slate-950 sm:text-5xl">Page not found</h1>
        <p className="mx-auto mt-5 max-w-xl text-lg leading-8 text-slate-600">
          The page you are looking for is not available in the current public frontend.
        </p>
        <div className="mt-8 flex flex-col justify-center gap-3 sm:flex-row">
          <Link to="/" className={buttonVariants({ className: "bg-blue-600 hover:bg-blue-700" })}>
            <ArrowLeft />
            Back Home
          </Link>
          <Link to="/search" className={buttonVariants({ variant: "outline" })}>
            <Search />
            Search Site
          </Link>
        </div>
      </Container>
    </section>
  );
}
