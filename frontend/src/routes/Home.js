import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/Home.module.css"
import MulMulCardView from "../component/MulMulCardView";
import Carousel from 'react-bootstrap/Carousel';
import KiriKiriCategoryRoundImage from "../component/KiriKiriCategoryRoundImage";
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import book from "../img/book.png"
import fashion from "../img/fashion.png"
import ticket from "../img/ticket.png"
import young from "../img/young.png"

const Home = () => {
    return (<body>
        <section className={styles.start}>
            <div className={styles.startLeft}>CO끼리</div>
            <div className={styles.startRight}>
                <div className={styles.startRight1}>
                    중고 거래부터 동네 인증까지, 코끼리와 함께해요.<br/>
                가볍고 따뜻한 코끼리를 만들어요.</div>
                <div className={styles.startRight2}>
                    <button>시작하기</button>
                    <button>내 물건 올리기</button>
                </div>
            </div>
        </section>
        <section className={styles.mulmulTrade}>
            <h2>우리 동네 인기 물물교환</h2>
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
        <footer>
            footer
        </footer>
        </body>
    );
}

export default Home;