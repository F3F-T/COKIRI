import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/MulmulTrade.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../img/cokkiriLogo.png"


const MulmulTrade = () => {
    let navigate = useNavigate();
    return (
            <div className={styles.mulmulTrade}>
                <div className={styles.mulmulTradeContent}>
                    <div className={styles.categoryBox}>
                        <div className={styles.forWho}>
                            <button className={styles.forWho_1} onClick={()=>navigate('/mulmultrade')}>이런 물건이 올라왔어요.</button>
                            <button className={styles.forWho_1} onClick={()=>navigate('/mulmultrade/mulmultrade2')}>이런 물건을 원해요.</button>
                            {/*<Navbar className={styles.postOrWant} bg="white" variant="white">*/}
                            {/*    <Nav className={styles.postOrWant_2}>*/}
                            {/*        <Nav.Link className={styles.forWho_1}  onClick={()=>navigate('/')}>이런 물건이 올라왔어요.</Nav.Link>*/}
                            {/*        <Nav.Link className={styles.forWho_1} onClick={()=>navigate('/')}>이런 물건을 원해요.</Nav.Link>*/}
                            {/*    </Nav>*/}
                            {/*</Navbar>*/}
                        </div>
                        <div className={styles.category}>
                            <button className ={styles.itemC}>전체</button>
                            <button className ={styles.itemC}>도서</button>
                            <button className ={styles.itemC}>생활가전</button>
                            <button className ={styles.itemC}>의류</button>
                            <button className ={styles.itemC}>유아도서</button>
                            <button className ={styles.itemC}>유아동</button>
                            <button className ={styles.itemC}>여성의류</button>
                            <button className ={styles.itemC}>남성의류</button>
                            <button className ={styles.itemC}>뷰티/미용</button>
                            <button className ={styles.itemC}>스포츠/레저</button>
                            <button className ={styles.itemC}>티켓/교환권</button>
                            <button className ={styles.itemC}>식물</button>
                            <button className ={styles.itemC}>가구</button>
                            <button className ={styles.itemC}>반려동물용품</button>
                            <button className ={styles.itemC}>가공용품</button>
                            <button className ={styles.itemC}>취미/게임</button>
                            <button className ={styles.itemC}>인테리어</button>
                            <button className ={styles.itemC}>생활/주방</button>
                             {/*각 버튼을 눌렀을 때, 00를 올린 사람들이에요.를 하단에 띄워줄거임*/}
                             {/*받아와서 매핑 후 다 url 만들어주는거겠지? 내일 얘기해봐야할듯*/}



                        </div>
                        <div className={styles.priceBox}>
                            <div className={styles.priceList}>
                                <button className={styles.priceI}>5천원~1만원</button>
                                <button className={styles.priceI}>1만원~3만원</button>
                                <button className={styles.priceI}>3만원~10만원</button>
                                <button className={styles.priceI}>10만원~20만원</button>
                                <button className={styles.priceI}>20만원~30만원</button>
                                <button className={styles.priceI}>30만원~50만원</button>
                                <button className={styles.priceI}>50만원~100만원</button>
                                <button className={styles.priceI}>100만원~</button>
                            </div>
                            <div className={styles.priceSet}>
                                    <input type="text" className={styles.setBox1}></input>
                                    <div className={styles.won}>~원</div>
                                    <input type="text" className={styles.setBox2}></input>
                            </div>
                        </div>
                    </div>
                    <div className={styles.navPostOrWant}>도서를 올린 사람들이에요</div>
                    <div className={styles.popularOrNewest}>
                        <div className={styles.pupularBtn}>인기도순</div>
                        <div className={styles.slash}>|</div>
                        <div className={styles.newsetBtn}>누적도순</div>
                    </div>
                    <div className={styles.postContainer}>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                        <div clasName={styles.postItem}><img className={styles.postImage} src = {myImage}/></div>
                    </div>
                </div>
            </div>
    );
}

export default MulmulTrade;