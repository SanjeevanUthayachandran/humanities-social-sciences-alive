import { Outlet } from "react-router-dom";
import Navbar from "@/components/navigation/Navbar";
import Footer from "@/components/footer/Footer";
import { useScrollToTop } from "@/hooks/useScrollToTop";

function MainLayout() {
  useScrollToTop();

  return (
    <>
      <Navbar />
      <main className="min-h-[calc(100vh-5rem)]">
        <Outlet />
      </main>
      <Footer />
    </>
  );
}

export default MainLayout;
