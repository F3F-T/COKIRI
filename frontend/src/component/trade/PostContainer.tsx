import React, { useEffect, useState } from 'react';
import styles from '../../styles/trade/PostContainer.module.css';
import { useNavigate } from 'react-router-dom';
import Card from '../tradeCard/Card';
import { useSelector } from 'react-redux';
import { Rootstate } from '../../index';
import Api from '../../utils/api';
import Loading from '../common/Loading';
import { useInView } from 'react-intersection-observer';


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

interface Page {
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

type categoryOption = 'wishCategory' | 'productCategory' | 'both'
type filtertype = 'recent' | 'popular'

interface postProps {
  categoryOption?: categoryOption,
  filterType?: filtertype,
  searchOption?: string;
}

const PostContainer = (postProps: postProps) => {
  const store = useSelector((state: Rootstate) => state);
  const navigate = useNavigate();
  //tagsearch에서 사용한 query string을 받아오기 위함
  const queryString = window.location.search;

  let wishCategory = '';
  let productCategory = '';
  let minPrice = '';
  let maxPrice = '';
  let sortType;

  // console.log(queryString);
  // console.log(typeof queryString)

  const detail = useSelector((state: Rootstate) => {
    return state.postDetailReducer;
  });


  const [postList, setPostList] = useState<PostType[]>(null);
  const [pageInfo, setPageInfo] = useState<Page>(null);
  const [loading, setLoading] = useState(false);
  let page;

  //무한 스크롤 구현
  // const loadingRef = useRef();
  const { ref, inView, entry } = useInView({
    /* Optional options */
    threshold: 0,
  });


  async function getMorePostList() {
    //interceptor를 사용한 방식 (header에 token값 전달)
    try {
      //query string 날리기
      if (queryString.length < 1 || !queryString.includes('?tags=')) {
        const currentPage = pageInfo.number;
        if (!pageInfo.last) {
          const res = await Api.get(`/post?productCategory=${productCategory}&wishCategory=${wishCategory}&minPrice=${minPrice}&maxPrice=${maxPrice}&sort=${sortType}&size=8&page=${currentPage + 1}`);

          setPageInfo(prevState => {
            return {
              empty: res.data.empty,
              first: res.data.first,
              last: res.data.last,
              number: res.data.number,
              numberOfElements: res.data.numberOfElements,
              size: res.data.size,
              totalElements: res.data.totalElements,
              totalPages: res.data.totalPages,
            };
          });
          // console.log(res);
          // console.log(res.data)
          setPostList(prevState => {
            return [...prevState, ...res.data.content];
          });
        }
      } else if (queryString.includes('?tags=')) {
        const res = await Api.get(`/post/tagSearch/${queryString}&sort=${sortType}&size=20&page=0`);
        // console.log(res)
        setPageInfo(prevState => {
          return {
            empty: res.data.empty,
            first: res.data.first,
            last: res.data.last,
            number: res.data.number,
            numberOfElements: res.data.numberOfElements,
            size: res.data.size,
            totalElements: res.data.totalElements,
            totalPages: res.data.totalPages,
          };
        });

        setPostList(prevState => {
          return [...prevState, ...res.data.content];
        });
      }
    } catch (err) {
      console.log(err);
      alert('get 실패');
    }
  }

  useEffect(() => {
    if (inView) {
      // console.log("스크롤의 끝입니다")
      getMorePostList();
    } else {
      // console.log("스크롤의 끝이 아니다")
    }
  }, [inView]);


  // console.log(page);


  if (postProps.categoryOption === 'wishCategory') {
    wishCategory = store.categoryReducer.category;
  } else if (postProps.categoryOption === 'productCategory') {
    productCategory = store.categoryReducer.category;
  } else if (postProps.categoryOption === 'both') {
    wishCategory = store.categoryReducer.category;
    productCategory = store.categoryReducer.category;
  }

  if (wishCategory === '전체') {
    wishCategory = '';
  }

  if (productCategory === '전체') {
    productCategory = '';
    // console.log(store.priceReducer.maxPrice);
    // console.log(store.priceReducer.minPrice);
  }

  if (store.priceReducer.minPrice != null) {
    minPrice = store.priceReducer.minPrice;
  }

  if (store.priceReducer.maxPrice != null) {
    maxPrice = store.priceReducer.maxPrice;
  }
  //최신순 필터링
  if (postProps.filterType === 'recent') {
    sortType = `createDate,DESC`;
  }
  //인기순 필터링 : scrap 순 -> messageRoom 순 -> post ID 역순(최신순)
  else if (postProps.filterType === 'popular') {
    sortType = `scrapPosts.size,DESC&messageRooms.size,DESC&sort=id,ASC`;
  }

  //홈 화면에서 인기매물로 들어갔을때
  if (queryString == '?sort=popular') {
    sortType = `scrapPosts.size,DESC&messageRooms.size,DESC&sort=id,ASC`;
  }

  async function getPostList() {
    //interceptor를 사용한 방식 (header에 token값 전달)
    try {
      setPostList(prevState => {
        return null;
      });
      // console.log(sortType);
      //query string 날리기
      if (queryString.length < 1 || !queryString.includes('?tags=')) {
        const res = await Api.get(`/post?productCategory=${productCategory}&wishCategory=${wishCategory}&minPrice=${minPrice}&maxPrice=${maxPrice}&sort=${sortType}&size=8&page=0`);
        // console.log(res);
        setPageInfo(prevState => {
          return {
            empty: res.data.empty,
            first: res.data.first,
            last: res.data.last,
            number: res.data.number,
            numberOfElements: res.data.numberOfElements,
            size: res.data.size,
            totalElements: res.data.totalElements,
            totalPages: res.data.totalPages,
          };
        });
        // console.log(res.data)
        setPostList(prevState => {
          return [...res.data.content];
        });
      } else if (queryString.includes('?tags=')) {
        const res = await Api.get(`/post/tagSearch/${queryString}&sort=${sortType}&size=10&page=0`);
        // console.log(res)
        setPageInfo(prevState => {
          return {
            empty: res.data.empty,
            first: res.data.first,
            last: res.data.last,
            number: res.data.number,
            numberOfElements: res.data.numberOfElements,
            size: res.data.size,
            totalElements: res.data.totalElements,
            totalPages: res.data.totalPages,
          };
        });

        setPostList(prevState => {
          return [...res.data.content];
        });
      }
    } catch (err) {
      console.log(err);
      alert('get 실패');
    }
  }

  // const target = useRef(null);
  //
  // useEffect(() => {
  //     observer.observe(target.current);
  // }, []);
  //
  // const options = {
  //     threshold: 1.0,
  // };
  //
  // const callback = () => {
  //     target.current.innerText += "관측되었습니다";
  // };
  //
  // const observer = new IntersectionObserver(callback, options);
  //

  // console.log(postProps.filterType);

  // getPostList();
  useEffect(() => {
    getPostList();
    // console.log(page)
  }, [wishCategory, productCategory, minPrice, maxPrice, postProps.filterType, store.refreshReducer.postChange]);

  /**
   * 중요) postList를 async로 받긴 하지만 받아오는 시간 전까지는 postList가 null이기 때문에 밑에있는 render 에서 postList.map 이 null을 접근하게 돼서 오류가 발생하고, 켜지지 않는다
   * 이렇게 예외처리를 꼭 해야한다.
   */
//

  // console.log(store.refreshReducer.postChange);
  if (!postList) {
    return <Loading />;
  }


  // if(!pageInfo)
  // {
  //     return null;
  // }


  const onClickPost = (post) => {
    console.log(post);
    console.log(post.id);
    navigate(`/post/${post.id}`);
  };

  return (
    <div>
      <div className={styles.postContainer}>
        {
          postList.map((post, index) => (
            <Card key={index} className={'forTrade'} like={post.scrapCount} postTitle={post.title}
                  postContent={post.content} wishCategory={post.wishCategory}
                  messageRoomCount={post.messageRoomCount}
                  onClick={() => {
                    onClickPost(post);
                  }} thumbnail={post.thumbnail} />
          ))
        }
      </div>
      <div className={styles.loadingDiv} ref={ref}>
        {
          pageInfo.last ? ' ' : <Loading />
        }
      </div>
    </div>
  );
};

export default PostContainer;