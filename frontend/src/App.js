
import React from "react";

import Home from "./routes/Home";
import { Route, Switch } from "react-router-dom";
import MyPage from "./routes/MyPage";
import MulmulTrade from "./routes/MulmulTrade";
import kiriKiriTrade from "./routes/KiriKiriTrade";
import KiriKiriTrade from "./routes/KiriKiriTrade";
import Nav from "./component/Nav";
function App() {
  return (
      <div className="App">
          {/*<Nav />*/}
        <Switch>
          <Route exact path="/" component={Home} />
          <Route path="/mulmultrade" component={ MulmulTrade} />
          <Route path="/kirikiritrade" component={KiriKiriTrade} />
          <Route path="/mypage" component={MyPage} />
        </Switch>
      </div>
  );
}

export default App;
