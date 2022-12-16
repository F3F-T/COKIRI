
import React from "react";

import Home from "./routes/Home";
import { Route, Routes } from "react-router-dom";
import MyPage from "./routes/MyPage";
import MulmulTrade from "./routes/MulmulTrade";
import kiriKiriTrade from "./routes/KiriKiriTrade";
import KiriKiriTrade from "./routes/KiriKiriTrade";
import Nav from "./component/Nav";
function App() {
  return (
      <div className="App">
          <Nav />
        <Routes>
          <Route exact path="/" element={<Home/>} />
          <Route path="/mulmultrade" element={ <MulmulTrade/>} />
          <Route path="/kirikiritrade" element={<KiriKiriTrade/>} />
          <Route path="/mypage" element={<MyPage/>} />
        </Routes>
      </div>
  );
}

export default App;
