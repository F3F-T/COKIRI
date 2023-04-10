import React, { useState } from 'react';
import styles from '../../styles/trade/Trade.module.css';
import PostContainer from '../../component/trade/PostContainer';
import { useDispatch, useSelector } from 'react-redux';
import { Rootstate } from '../../index';
import { changePostRefreshState } from '../../store/refreshReducer';


const MulmulTrade1 = () => {
  const [tab1, setTab] = useState('next');
//
  const dispatch = useDispatch();
  type filtertype = 'recent' | 'popular'
  const [filterType, setFilterType] = useState<filtertype>('recent');


  function setDealTab(tab) {
    setTab(tab);
  }

  const store = useSelector((state: Rootstate) => state);
  const categories2: string[] =
    ['식품', '티켓/교환권', '서비스/기술',
      '유아동용품', '운동용품', '식물', '취미/게임', '반려동물용품', '생활/주방'];
  return (
    <div className={styles.mulmulTrade}>
      <div className={styles.mulmulTradeContent}>
        {store.categoryReducer.category === '전체' ? <div className={styles.navPostOrWant}>전체 게시글이에요.</div> :
          categories2.includes(store.categoryReducer.category) ?
            <div className={styles.navPostOrWant}>{store.categoryReducer.category}을 올린 사람들이에요 🙌</div> :
            <div className={styles.navPostOrWant}>{store.categoryReducer.category}를 올린 사람들이에요 🙌</div>
        }


        <div className={styles.popularOrNewest}>
          {tab1 === 'next' ? <button className={`${styles['newsetBtn' + (tab1 === 'next' ? 'active' : '')]}`}
                                     onClick={() => {
                                       setDealTab('next');
                                       setFilterType('recent');
                                       dispatch(changePostRefreshState());
                                     }}>✓최신순</button>
            : <button className={`${styles['newsetBtn' + (tab1 === 'next' ? 'active' : '')]}`}
                      onClick={() => {
                        setDealTab('next');
                        setFilterType('recent');
                        dispatch(changePostRefreshState());
                      }}>최신순</button>
          }
          {tab1 === 'curr' ? <button className={`${styles['pupularBtn' + (tab1 === 'curr' ? 'active' : '')]}`}
                                     onClick={() => {
                                       setDealTab('curr');
                                       setFilterType('popular');
                                       dispatch(changePostRefreshState());
                                     }}>✓인기도순</button>
            : <button className={`${styles['pupularBtn' + (tab1 === 'curr' ? 'active' : '')]}`}
                      onClick={() => {
                        setDealTab('curr');
                        setFilterType('popular');
                        dispatch(changePostRefreshState());
                      }}>인기도순</button>
          }
        </div>
        <PostContainer categoryOption={'productCategory'} filterType={filterType} />
      </div>
    </div>
  );
};

export default MulmulTrade1;