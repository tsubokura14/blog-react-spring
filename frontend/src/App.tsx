import { Routes, Route } from 'react-router-dom';
import PostListPage from './pages/PostListPage';
import PostFormPage from './pages/PostFormPage';
import PostDetailPage from './pages/PostDetailPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';

function App() {
  return (
    <Routes>
      <Route path="/" element={<PostListPage />} />
      <Route path="/posts/new" element={<PostFormPage />} />
      <Route path="/posts/:id" element={<PostDetailPage />} />
      <Route path="/posts/id/edit" element={<PostFormPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
    </Routes>
  );
}

export default App;