import React, { useEffect, useState } from 'react';
import styles from '../../styles/trade/TradeCategory.module.css';

import { Rootstate } from '../../index';
import { useDispatch, useSelector } from 'react-redux';
import { storeCategory } from '../../store/categoryReducer';
import classNames from 'classnames/bind';

//TODO: REFACTORING) categories를 배열로 미리 만들고 map함수로 생성
const TradeCategory = () => {

  const cx = classNames.bind(styles);

  //index에서 선언해준 Rootstate를 state로 받는다, store에 저장한다.
  const store = useSelector((state: Rootstate) => state);
  //action을 사용하기 위해 dispatch를 선언한다.
  const dispatch = useDispatch();
  const [isClicked, setIsClicked] = useState<number>();
  const categories: string[] =
    ['전체', '도서', '식품', '생활가전', '유아도서', '유아동', '여성의류', '남성의류', '뷰티/미용', '스포츠/레저',
      '티켓/교환권', '식물', '가구', '반려동물용품', '가공용품', '취미/게임', '인테리어', '생활/주방'];


  const onClickCategoryButton = (e, category) => {
    dispatch(storeCategory(category));
    setIsClicked((prev) => {
      return e.target.value;
    });

    console.log(isClicked);

    //확인하고 싶을때 : console.log(store.categoryReducer.category)
    //store : index.tsx에 있는 store
    //categoryReducer : store ->  reducer안에 선언한 categoryReducer, 이는 categoryReducer.ts를 참조한다.
    //category : categoryReducer.ts 안에 있는 categorySlice에 담긴 category이다. 이는 initialState : 안에 선언이 되어있다.
  };

  useEffect(() => {
    if (store.categoryReducer.category === '도서') {
      setIsClicked(prevState => {
        return 1;
      });
    } else if (store.categoryReducer.category === '남성의류') {
      setIsClicked(prevState => {
        return 7;
      });
    } else if (store.categoryReducer.category === '티켓/교환권') {
      setIsClicked(prevState => {
        return 10;
      });
    } else if (store.categoryReducer.category === '유아동') {
      setIsClicked(prevState => {
        return 5;
      });
    }


  }, []);

  return (

    <div className={styles.category}>
      {categories.map((category: string, index) => (
        <button value={index} className={cx('itemC', index != isClicked ? `` : `colored`)}
                onClick={(event) => onClickCategoryButton(event, category)}>{category}</button>
      ))}
      <div>
        {/*각 버튼을 눌렀을 때, 00를 올린 사람들이에요.를 하단에 띄워줄거임*/}
        {/*받아와서 매핑 후 다 url 만들어주는거겠지? 내일 얘기해봐야할듯*/}
      </div>
    </div>
  );
};

export default TradeCategory;