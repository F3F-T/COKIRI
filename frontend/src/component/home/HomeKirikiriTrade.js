import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/home/HomeKirikiriTrade.module.css";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import KiriKiriCategoryRoundImage from "../KiriKiriCategoryRoundImage";
import book from "../../img/book.png";
import fashion from "../../img/fashion.png";
import ticket from "../../img/ticket.png";
import young from "../../img/young.png";
import Carousel from "react-bootstrap/Carousel";
import MulMulCardView from "../MulMulCardView";

const HomeKirikiriTrade = () => {
    return (
        <section className={styles.kirikiriTrade}>
            <h2>다른 카테고리 뿐만 아니라 같은 카테고리 끼리도 교환할 수 있어요</h2>

            <div className={styles.kirikiriCatagoryCardView}>
                <Row>
                    <Col xs={3}>
                        <KiriKiriCategoryRoundImage props={book}/>
                    </Col>
                    <Col xs={3}>
                        <KiriKiriCategoryRoundImage props={fashion}/>
                    </Col>
                    <Col xs={3}>
                        <KiriKiriCategoryRoundImage props={ticket}/>
                    </Col>
                    <Col xs={3}>
                        <KiriKiriCategoryRoundImage props={young}/>
                    </Col>
                </Row>
            </div>;



            <Carousel>
                <Carousel.Item interval={5000}>
                    <div className={styles.mulmulCardView}>
                        <MulMulCardView/>
                        <MulMulCardView/>
                        <MulMulCardView/>
                        <MulMulCardView/>
                        <div>
                        </div>
                    </div>
                </Carousel.Item>

                <Carousel.Item interval={5000}>
                    <div className={styles.mulmulCardView}>
                        <MulMulCardView/>
                        <MulMulCardView/>
                        <MulMulCardView/>
                        <MulMulCardView/>
                    </div>
                </Carousel.Item>
            </Carousel>
        </section>
    );
}

export default HomeKirikiriTrade;