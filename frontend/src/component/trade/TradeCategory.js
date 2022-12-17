import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/trade/TradeCategory.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';

const TradeCategory = () => {
    return (
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
    );
}

export default TradeCategory;