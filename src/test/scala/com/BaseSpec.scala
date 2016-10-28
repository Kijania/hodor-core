package com

import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

abstract class BaseSpec extends WordSpec with Matchers with MockitoSugar
