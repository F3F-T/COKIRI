
import React from "react";

import Home from "./routes/Home";
import { Route, Switch } from "react-router-dom";
import MyPage from "./routes/MyPage";
import MulmulTrade from "./routes/MulmulTrade";
import kiriKiriTrade from "./routes/KiriKiriTrade";
import KiriKiriTrade from "./routes/KiriKiriTrade";
import Nav from "./component/Nav";
import styles from "./styles/App.module.css";
function App() {
  return (
      <div>
      <Nav />
      <div className={styles.content}>
        <Switch>
          <Route exact path="/" component={Home} />
          <Route path="/mulmultrade" component={ MulmulTrade} />
          <Route path="/kirikiritrade" component={KiriKiriTrade} />
          <Route path="/mypage" component={MyPage} />
        </Switch>
      </div>
      </div>
  );
}

export default App;
