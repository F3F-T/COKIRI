import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/home/Home.module.css"
import MulMulCardView from "../component/home/MulMulCardView";
import Carousel from 'react-bootstrap/Carousel';
import KiriKiriCategoryRoundImage from "../component/home/KiriKiriCategoryRoundImage";
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import book from "../img/book.png"
import fashion from "../img/fashion.png"
import ticket from "../img/ticket.png"
import young from "../img/young.png"
import HomeStart from "../component/home/HomeStart";
import HomeMulmulTrade from "../component/home/HomeMulmulTrade";
import HomeKirikiriTrade from "../component/home/HomeKirikiriTrade";

const Home = () => {
    return (
        <body>
        <HomeStart/>
        <HomeMulmulTrade/>
        <HomeKirikiriTrade/>
        </body>
    );
}

export default Home;