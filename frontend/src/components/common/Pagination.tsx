import { ChevronLeft, ChevronRight } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";

interface PaginationProps {
  currentPage: number;
  pageCount: number;
  onPageChange: (page: number) => void;
}

export default function Pagination({ currentPage, pageCount, onPageChange }: PaginationProps) {
  if (pageCount <= 1) {
    return null;
  }

  return (
    <nav className="mt-10 flex items-center justify-center gap-2" aria-label="Pagination">
      <Button
        type="button"
        variant="outline"
        size="icon"
        onClick={() => onPageChange(Math.max(1, currentPage - 1))}
        disabled={currentPage === 1}
        aria-label="Previous page"
      >
        <ChevronLeft />
      </Button>
      {Array.from({ length: pageCount }, (_, index) => index + 1).map((page) => (
        <Button
          key={page}
          type="button"
          variant={page === currentPage ? "default" : "outline"}
          size="icon"
          onClick={() => onPageChange(page)}
          className={cn(page === currentPage && "bg-blue-600 hover:bg-blue-700")}
          aria-label={`Go to page ${page}`}
          aria-current={page === currentPage ? "page" : undefined}
        >
          {page}
        </Button>
      ))}
      <Button
        type="button"
        variant="outline"
        size="icon"
        onClick={() => onPageChange(Math.min(pageCount, currentPage + 1))}
        disabled={currentPage === pageCount}
        aria-label="Next page"
      >
        <ChevronRight />
      </Button>
    </nav>
  );
}
