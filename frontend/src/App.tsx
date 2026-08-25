import { Routes, Route } from 'react-router-dom';
import PostListPage from './pages/PostListPage';

function App() {
  return (
    <Routes>
      <Route path="/" element={<PostListPage />} />
    </Routes>
  );
}

export default App;