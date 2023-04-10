import React, { useCallback, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/home/Home.module.scss';
import book from '../img/book.png';
import fashion from '../img/fashion.png';
import ticket from '../img/ticket.png';
import young from '../img/young.png';
import { useSelector } from 'react-redux';
import { Rootstate } from '../index';
import Api from '../utils/api';
import HomePostCardSwiper from '../component/common/HomePostCardSwiper';
import RoundImageSwiper from '../component/common/RoundImageSwiper';
import classNames from 'classnames/bind';
import Modal from './로그인 & 회원가입/ModalList';

const cx = classNames.bind(styles);

//모르는 태그가 너무 많아 하다가 멈춤
//허락 맡고 다시 진행 예정


const directionButtons = (direction) => {
  return (
    <span
      aria-hidden='true'
      className={direction === 'Next' ? 'button-next' : 'button-prev'}
    >
        {direction}
      </span>
  );
};

interface PostType {
  id?: number;
  title?: string;
  content?: string;
  tradeEachOther?: boolean;
  authorNickname?: string;
  wishCategory?: string;
  productCategory?: string;
  tradeStatus?: string;
  tagNames?: string[];
  scrapCount?: number;
  messageRoomCount?: number;
  thumbnail?: string;
}

const HomeMulmulTrade = () => {
  const [postList, setPostList] = useState<PostType[]>(null);
  const navigate = useNavigate();

  async function getPostList() {
    //interceptor를 사용한 방식 (header에 token값 전달)
    try {
      //query string 날리기
      const res = await Api.get(`/post?&sort=scrapPosts.size,DESC&messageRooms.size,DESC&sort=id,ASC&size=10&page=0`);
      console.log(res);

      console.log(res.data);
      setPostList(prevState => {
        return [...res.data.content];
      });

    } catch (err) {
      console.log(err);
      alert('get 실패');
    }
  }

  const onClickMore = () => {
    navigate(`/mulmultrade?sort=popular`);
  };

  useEffect(() => {
    getPostList();
  }, []);

  console.log(postList);


  return (
    <section className={styles.mulmulTrade}>
      <div className={styles.tradeTop}>
        <h2>우리 동네의 인기있는 물물교환 아이템들 👏</h2>
        <li onClick={onClickMore}>더보기</li>
      </div>
      <div className={styles.mulmulCardView}>
        <div className={'homeSwiper'}>
          <HomePostCardSwiper postList={postList} />
        </div>
      </div>

    </section>
  );
};

const HomeKirikiriTrade = () => {

  const [postList, setPostList] = useState<PostType[]>(null);
  const navigate = useNavigate();
  const store = useSelector((state: Rootstate) => state);
  const category = store.categoryReducer.category;

  async function getPostList() {
    //interceptor를 사용한 방식 (header에 token값 전달)
    try {
      //query string 날리기
      const res = await Api.get(`/post?productCategory=${category}&wishCategory=${category}&sort=scrapPosts.size,DESC&messageRooms.size,DESC&sort=id,ASC&size=10&page=0`);
      console.log(res);

      console.log(res.data);
      setPostList(prevState => {
        return [...res.data.content];
      });

    } catch (err) {
      console.log(err);
      alert('get 실패');
    }
  }

  const onClickMore = () => {
    navigate(`/mulmultrade?sort=popular?category=${category}`);
  };

  useEffect(() => {
    getPostList();
  }, [store.categoryReducer.category]);

  console.log(postList);

  return (
    <>
      <section className={styles.kirikiriTrade}>
        <hr className={styles.hrFull} />
        <div className={styles.kiriTop}>
          <h2>다른 카테고리 뿐만 아니라 같은 카테고리끼리도 교환할 수 있어요 👇 </h2>
        </div>

        <div className={styles.kirikiriCatagoryCardView}>
          <div className={'roundImageSwiper'}>
            <RoundImageSwiper imageList={[book, fashion, ticket, young, book, fashion, ticket, young]} />
          </div>
        </div>
        <div className={styles.mulmulCardView}>
          <li className={styles.kiriLi} onClick={onClickMore}>더보기</li>
          <div className={'homeSwiper'}>
            <HomePostCardSwiper postList={postList} />
          </div>
        </div>

      </section>
    </>

  );
};

const HomeStart = () => {
  const navigate = useNavigate();
  const store = useSelector((state: Rootstate) => state);
  const onClickStart = () => {
    navigate(`/login`);
  };

  const onClickUpload = () => {
    navigate(`/upload`);
  };
  const [isOpenModal, setOpenModal] = useState<boolean>(false);
  const onClickToggleModal = useCallback(() => {
    setOpenModal(!isOpenModal);
  }, [isOpenModal]);
  return (
    <div className={styles.boxbox}>
      {isOpenModal && (
        <Modal onClickToggleModal={onClickToggleModal}>
          <embed type='text/html' width='800' height='608' />
        </Modal>
      )}
      <section className={styles.start}>
        <div className={styles.startLeft}>CO끼리</div>
        <div className={styles.startRight}>
          <div className={styles.startRight1}>
            사용하지 않는 물건이 있나요? <br />
            끼리끼리 교환하며 새로운 가치를 만나보세요.
          </div>
          <div className={styles.startRight2}>
            {
              store.userInfoReducer.id == null ?
                <button className={cx('startBtn')} onClick={onClickStart}>시작하기</button> :
                <></>

            }

            {/*<button className={cx('startBtn')} onClick={onClickUpload}>내 물건 올리기</button>*/}
            <button className={cx('startBtn')} onClick={() => onClickToggleModal()}>내 물건 올리기</button>

            {/*<Button className={"lightblue"} content={"시작하기"} onClick={onClickStart} color={"black"} hover={true} size={"medium"}/>*/}
            {/*<Button className={"lightblue"} content={"내 물건 올리기"} onClick={onClickUpload} color={"black"} hover={true} size={"medium"}/>*/}

          </div>
        </div>

      </section>

    </div>
  );
};
const Home = () => {
  const navigate = useNavigate();
  const store = useSelector((state: Rootstate) => state);
  const onClickStart = () => {
    navigate(`/login`);
  };

  const onClickUpload = () => {
    navigate(`/upload`);
  };
  const [isOpenModal, setOpenModal] = useState<boolean>(false);
  const onClickToggleModal = useCallback(() => {
    setOpenModal(!isOpenModal);
  }, [isOpenModal]);
  return (
    <div className={styles.wrap}>
      <div className={styles.boxbox}>
        {isOpenModal && (
          <Modal onClickToggleModal={onClickToggleModal}>
            <embed type='text/html' width='800' height='608' />
          </Modal>
        )}
        <div className={styles.home}>
          <section className={styles.start}>
            <div className={styles.startLeft}>CO끼리</div>
            <div className={styles.startRight}>
              <div className={styles.startRight1}>
                사용하지 않는 물건이 있나요? <br />
                끼리끼리 교환하며 새로운 가치를 만나보세요.
              </div>
              <div className={styles.startRight2}>
                {
                  store.userInfoReducer.id == null ?
                    <button className={cx('startBtn')} onClick={onClickStart}>시작하기</button> :
                    <></>

                }

                {/*<button className={cx('startBtn')} onClick={onClickUpload}>내 물건 올리기</button>*/}
                {
                  store.userAddressInfoReducer.addressName1 == null ?
                    <button className={cx('startBtn')} onClick={() => onClickToggleModal()}>내 물건 올리기</button>
                    :
                    <button className={cx('startBtn')} onClick={onClickUpload}>내 물건 올리기</button>


                }

                {/*<Button className={"lightblue"} content={"시작하기"} onClick={onClickStart} color={"black"} hover={true} size={"medium"}/>*/}
                {/*<Button className={"lightblue"} content={"내 물건 올리기"} onClick={onClickUpload} color={"black"} hover={true} size={"medium"}/>*/}

              </div>
            </div>

          </section>
          <HomeMulmulTrade />
          <HomeKirikiriTrade />
        </div>
      </div>
      {/*<Footer/>*/}

    </div>
  );
};

export default Home;