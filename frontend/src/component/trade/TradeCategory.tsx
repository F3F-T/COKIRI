import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/trade/TradeCategory.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';

import {Rootstate} from "../../index";
import {useDispatch, useSelector} from "react-redux";
import {Root} from "react-dom/client";
import {resetCategory, storeCategory} from "../../store/categoryReducer";

//TODO: REFACTORING) categories를 배열로 미리 만들고 map함수로 생성
const TradeCategory = () => {

    //index에서 선언해준 Rootstate를 state로 받는다, store에 저장한다.
    const store = useSelector((state:Rootstate) => state);
    //action을 사용하기 위해 dispatch를 선언한다.
    const dispatch = useDispatch();


    const categories: string[] =
        ['전체', '도서', '생활가전', '의류', '유아도서', '유아동', '여성의류', '남성의류', '뷰티/미용', '스포츠/레저',
            '티켓/교환권', '식물', '가구', '반려동물용품', '가공용품', '취미/게임', '인테리어', '생활/주방']
    // const categories: string[] =
    //     ['전체', '도서', '의류', '유아도서', '여성의류', '남성의류', '스포츠/레저', '가구', '인테리어']
    // const categories2: string[]=
    //     ['생활가전',  '유아동',  '뷰티/미용',
    //     '티켓/교환권', '식물', '반려동물용품', '가공용품', '취미/게임', '생활/주방']
    const onClickCategoryButton = (category)  => {
        dispatch(storeCategory(category))
        //확인하고 싶을때 : console.log(store.categoryReducer.category)
        //store : index.tsx에 있는 store
        //categoryReducer : store ->  reducer안에 선언한 categoryReducer, 이는 categoryReducer.ts를 참조한다.
        //category : categoryReducer.ts 안에 있는 categorySlice에 담긴 category이다. 이는 initialState : 안에 선언이 되어있다.
    }


    return (

        <div className={styles.category}>
                {categories.map((category:string)=> (
                        <button key={category} className={styles.itemC} onClick={() => onClickCategoryButton(category)}>{category}</button>
                ))}
        <div>
            {/*각 버튼을 눌렀을 때, 00를 올린 사람들이에요.를 하단에 띄워줄거임*/}
            {/*받아와서 매핑 후 다 url 만들어주는거겠지? 내일 얘기해봐야할듯*/}
        </div>
        </div>
    );
}

export default TradeCategory;