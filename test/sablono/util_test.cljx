(ns sablono.util-test
  #+cljs (:require-macros [cemerick.cljs.test :refer [are is deftest testing]]
                          [sablono.core :refer [with-base-url]])
  #+cljs (:import goog.Uri)
  (:require [sablono.util :as u]
            #+clj [clojure.test :refer :all]
            #+cljs [cemerick.cljs.test :as t]))

(deftest test-merge-with-class
  (are [maps expected]
    (is (= expected (apply u/merge-with-class maps)))
    [] nil
    [{:a 1} {:b 2}]
    {:a 1 :b 2}
    [{:a 1 :class :a} {:b 2 :class "b"} {:c 3 :class ["c"]}]
    {:a 1 :b 2 :c 3 :class [:a "b" "c"]}))

(deftest test-normalize-element
  (are [element expected]
    (is (= expected (u/normalize-element element)))
    [:div] ["div" {} nil]
    [:div#foo] ["div" {:id "foo"} nil]
    [:div.foo] ["div" {:class ["foo"]} nil]
    [:div.a.b] ["div" {:class ["a" "b"]} nil]
    [:div.a.b {:class "c"}] ["div" {:class ["a" "b" "c"]} nil]))

(deftest test-react-symbol
  (are [tag expected]
    (is (= expected (u/react-symbol tag)))
    :div 'js/React.DOM.div
    "div" 'js/React.DOM.div))

#+cljs
(deftest test-as-str
  (are [args expected]
    (is (= expected (apply u/as-str args)))
    ["foo"] "foo"
    [:foo] "foo"
    [100] "100"
    ["a" :b 3] "ab3"
    [(Uri. "/foo")] "/foo"
    [(Uri. "localhost:3000/foo")] "localhost:3000/foo"))

#+cljs
(deftest test-to-uri
  (testing "with no base URL"
    (are [obj expected]
      (is (= expected (u/to-str (u/to-uri obj))))
      "foo" "foo"
      "/foo/bar" "/foo/bar"
      "/foo#bar" "/foo#bar"))
  (testing "with base URL"
    (with-base-url "/foo"
      (are [obj expected]
        (is (= expected (u/to-str (u/to-uri obj))))
        "/bar" "/foo/bar"
        "http://example.com" "http://example.com"
        "https://example.com/bar" "https://example.com/bar"
        "bar" "bar"
        "../bar" "../bar"
        "//example.com/bar" "//example.com/bar")))
  (testing "with base URL for root context"
    (with-base-url "/"
      (are [obj expected]
        (is (= expected (u/to-str (u/to-uri obj))))
        "/bar" "/bar"
        "http://example.com" "http://example.com"
        "https://example.com/bar" "https://example.com/bar"
        "bar" "bar"
        "../bar" "../bar"
        "//example.com/bar" "//example.com/bar")))
  (testing "with base URL containing trailing slash"
    (with-base-url "/foo/"
      (are [obj expected]
        (is (= expected (u/to-str (u/to-uri obj))))
        "/bar" "/foo/bar"
        "http://example.com" "http://example.com"
        "https://example.com/bar" "https://example.com/bar"
        "bar" "bar"
        "../bar" "../bar"
        "//example.com/bar" "//example.com/bar"))))
