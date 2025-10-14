import Header from "../../components/Header/Header";
import Parallax from "../../components/Parallax/Parallax";
import Footer from "../../components/Parallax/Footer";
import FoodSection from "../../components/FoodSection/FoodSection";
import HotelService from "../../components/Parallax/HotelService";

const Home = () => {
  return (
    <>
      <main className="container">
        <Header />

        <FoodSection title="Top Picks for You" />

        {/* <Parallax />

        <FoodSection title="Chef's Specials" /> */}

      </main>
      
      <Footer />
    </>
  );
};

export default Home;
