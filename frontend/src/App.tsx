import AppRouter from "@/routes/AppRouter";
import { ContentProvider } from "@/context/ContentContext";

function App() {
  return (
    <ContentProvider>
      <AppRouter />
    </ContentProvider>
  );
}

export default App;
